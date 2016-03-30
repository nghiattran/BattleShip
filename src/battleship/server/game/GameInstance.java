package battleship.server.game;

import java.nio.ByteBuffer;

import battleship.client.gui.structures.GridTile;
import battleship.client.gui.structures.Hitmarker;
import battleship.server.Server;
import battleship.server.game.player.Player;

/**
 * An instance of a game
 * GameInstance.java
 * @author Clayton Williams
 * @date Mar 5, 2016
 */
public class GameInstance {
	
	/**
	 * The players in our game
	 */
	private Player player1, player2;
	
	/**
	 * The current turn
	 */
	private int turn = 0;
	
	/**
	 * GameInstance
	 * @param player1 - the first player
	 * @param player2 - the second player
	 */
	public GameInstance(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
		player1.setQueued(false);
		player2.setQueued(false);
		player1.reset();
		player2.reset();
		ByteBuffer b = ByteBuffer.allocate(50);
		b.put((byte) 2);
		b.put(this.player2.getShipsAsBuffer());
		this.player1.getSession().write(b);
		ByteBuffer b2 = ByteBuffer.allocate(50);
		b2.put((byte) 2);
		b2.put(this.player1.getShipsAsBuffer());
		this.player2.getSession().write(b2);
		setTurn(Math.random() >= 0.50d ? player1 : player2);
	}
	
	/**
	 * Attempts to hit
	 * @param x - the x
	 * @param y - the y
	 */
	public void hit(int x, int y) {
		Player firingPlayer = turn == 0 ? player1 : player2;
		Player enemy = turn == 0 ? player2 : player1;
		boolean hit = enemy.isValidHit(x, y);
		firingPlayer.sendUpdateGridTile(new GridTile(x, y, hit ? Hitmarker.HIT : Hitmarker.MISS), true); 
		enemy.updateGridTile(new GridTile(x, y), hit ? Hitmarker.HIT : Hitmarker.MISS);
		setTurn(turn == 0 ? player2 : player1);
	}
	
	/**
	 * Ends the game 
	 * @param winner - the winning player
	 */
	public void endGame(Player winner) {
		if (winner != null) {
			ByteBuffer b = ByteBuffer.allocate(2);
			b.put((byte) 9);
			b.put((byte) 1);
			winner.getSession().write(b);
		}
		Player loser = winner.equals(player1) ? player2 : player1;
		if (loser != null) {
			ByteBuffer b = ByteBuffer.allocate(2);
			b.put((byte) 9);
			b.put((byte) 0);
			loser.getSession().write(b);
		}
		Server.getInstance().getGameEngine().deregisterGame(this);
	}
	
	/**
	 * Forfeits the game
	 * @param player
	 */
	public void forfeit(Player player) {
		endGame(player.equals(player1) ? player2 : player1);
	}
	
	/**
	 * Pulses the game
	 */
	public void pulse() {
		if (!player1.getSession().isActive() || !player2.getSession().isActive()) {
			System.out.println("Client disconnection from game");
			if (!player1.getSession().isActive()) {
				endGame(player2);
			} else {
				endGame(player1);
			}
		}	
		
		if (player1.isLost()) {
			System.out.println("Player 1 lost");
			endGame(player2);
		} else if (player2.isLost()) {
			System.out.println("Player 2 lost");
			endGame(player1);
		}
	}
	
	/**
	 * Checks if this game contains the specified player
	 * @param player
	 * @return
	 */
	public boolean containsPlayer(Player player) {
		return player1.equals(player) || player2.equals(player);
	}
	
	/**
	 * Sets the turn to the specified player
	 * @param player - the player to set the turn to
	 */
	private void setTurn(Player player) {
		turn = player.equals(player1) ? 0 : 1;
		ByteBuffer b = ByteBuffer.allocate(200);
		b.put((byte) 3);
		b.put((byte) 1);
		ByteBuffer b2 = ByteBuffer.allocate(200);
		b2.put((byte) 3);
		b2.put((byte) 0);
		player1.getSession().write(player.equals(player1) ? b : b2);
		player2.getSession().write(player.equals(player2) ? b : b2);
	}

}
