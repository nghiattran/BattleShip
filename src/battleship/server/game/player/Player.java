package battleship.server.game.player;

import java.nio.ByteBuffer;

import battleship.client.gui.Application;
import battleship.client.gui.component.impl.Ship;
import battleship.client.gui.structures.GridTile;
import battleship.client.gui.structures.Hitmarker;
import battleship.client.gui.structures.Orientation;
import battleship.client.gui.structures.ShipType;
import battleship.server.net.IoSession;

/**
 * Player.java - represents a player
 * @author Clayton Williams
 * @date Feb 16, 2016
 */
public class Player {
	
	/**
	 * The player's iosession
	 */
	private IoSession session;
	
	/**
	 * The player's ships
	 */
	private Ship[] ships = new Ship[ShipType.values().length];
	
	/**
	 * The tiles
	 */
	private GridTile[][] tiles = new GridTile[10][10];
	
	/**
	 * If the player is queued to find a game
	 */
	private boolean queued = false;
	
	/**
	 * Player
	 */
	public Player(IoSession session) {
		this.session = session;
		reset();
	}
	
	/**
	 * Checks if the x and y is a valid hit on this player's grid
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isValidHit(int x, int y) {
		GridTile tile = tiles[x][y];
		if (tile.getHitmarker() == Hitmarker.HIT)
			return true;
		for (Ship s : ships) {
			Orientation o = s.getOrientation();
			GridTile shipTile = new GridTile((int) s.getLocation().getX(), (int) s.getLocation().getY());
			ShipType shipType = s.getShipType();
			if (o == Orientation.HORIZONTAL && y == shipTile.getY() && x >= shipTile.getX() && x <= (shipTile.getX() + (shipType.getTileLength() - 1))) {
				return true;
			} else if (o == Orientation.VERTICAL && x == shipTile.getX() && y >= shipTile.getY() && y <= (shipTile.getY() + (shipType.getTileLength() - 1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the player lost
	 * @return
	 */
	public boolean isLost() {
		for (Ship s : ships) {
			Orientation o = s.getOrientation();
			GridTile shipTile = new GridTile((int) s.getLocation().getX(), (int) s.getLocation().getY());
			ShipType shipType = s.getShipType();
			for (int x = shipTile.getX(); x <= ((o == Orientation.HORIZONTAL) ? (shipTile.getX() + shipType.getTileLength() - 1) : shipTile.getX()); x++) {
				for (int y = shipTile.getY(); y <= ((o == Orientation.VERTICAL) ? (shipTile.getY() + shipType.getTileLength() - 1) : shipTile.getY()); y++) {
					if (tiles[x][y].getHitmarker() != Hitmarker.HIT) {
						return false;
					}
				}
			}
		}	
		return true;
	}
	
	/**
	 * Updates & sends a packet for updating a grid tile
	 * @param tile - the tile
	 * @param marker - the new marker
	 * @param enemy - if it's an enemy grid
	 */
	public void updateGridTile(GridTile tile, Hitmarker marker) {
		tiles[tile.getX()][tile.getY()].setHitmarker(marker);
		sendUpdateGridTile(tiles[tile.getX()][tile.getY()], false);
	}
	
	/**
	 * Sends an update 
	 * @param tile
	 * @param enemy
	 */
	public void sendUpdateGridTile(GridTile tile, boolean enemy) {
		ByteBuffer b = ByteBuffer.allocate(100);
		b.put((byte) 4);
		b.put((byte) tile.getX());
		b.put((byte) tile.getY());
		b.put((byte) tile.getHitmarker().ordinal());
		b.put((byte) (enemy ? 1 : 0));
		session.write(b);
	}
	
	/**
	 * Resets a player's dynamic variables
	 */
	public void reset() {
		for (int i = 0; i < tiles.length; i++) {
			for (int i2 = 0; i2 < tiles[i].length; i2++) {
				tiles[i][i2] = new GridTile(i, i2);
			}
		}
	}
	
	/**
	 * Gets the ships as a buffer
	 * @return
	 */
	public ByteBuffer getShipsAsBuffer() {
		ByteBuffer b = ByteBuffer.allocate(3000);
		for (int i = 0; i < ships.length; i++) {
			Ship s = ships[i];
			b.put((byte) s.getShipType().ordinal());
			b.put((byte) s.getX());
			b.put((byte) s.getY());
			b.put((byte) s.getOrientation().ordinal());
		}
		b.flip();
		return b;
	}
	
	/**
	 * Called when a player disconnects from the server
	 */
	public void disconnect() {
		
	}
	
	/**
	 * Gets the player's ships
	 * @return Ship[]
	 */
	public Ship[] getShips() {
		return this.ships;
	}
	
	/**
	 * Gets the player's tiles
	 * @return
	 */
	public GridTile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Gets the player's iosession
	 * @return {IoSession}
	 */
	public IoSession getSession() {
		return this.session;
	}
	
	/**
	 * If the player is queued for a game
	 * @return boolean
	 */
	public boolean isQueued() {
		return this.queued;
	}
	
	/**
	 * Sets the queued
	 * @param queued
	 */
	public void setQueued(boolean queued) {
		this.queued = queued;
	}

}
