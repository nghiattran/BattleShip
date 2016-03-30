package battleship.client.gui.structures;

import java.awt.Point;
import java.io.Serializable;

import battleship.client.gui.component.impl.BattleGrid.Grid;

/**
 * GridTile.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class GridTile implements Serializable {
	
	/**
	 * Serial (for transfer handler)
	 */
	private static final long serialVersionUID = -2756914211240329469L;

	/**
	 * Gets a grid tile for the mouse location
	 * @param mouseLocation - the mouse location
	 * @return {GridTile}
	 */
	public static GridTile forMouseLocation(Point mouseLocation) {	
		return new GridTile((int) Math.abs(Math.floor(mouseLocation.getX() / Grid.GRID_SPACING)), (int) Math.abs(Math.floor(mouseLocation.getY() / Grid.GRID_SPACING)));
	}
		
	/**
	 * The x and y of the grid tile
	 */
	private int x, y;
	
	/**
	 * Represents a hitmarker
	 */
	private Hitmarker hitmarker = Hitmarker.NONE;
	
	/**
	 * GridTile
	 */
	public GridTile(int x, int y) {
		this.x = x;
		this.y = y;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
	}
	
	/**
	 * GridTile
	 * @param x
	 * @param y
	 * @param marker
	 */
	public GridTile(int x, int y, Hitmarker marker) {
		this(x, y);
		this.hitmarker = marker;
	}
	
	/**
	 * Gets the pixel base of the tile
	 * @return {Point} - the base
	 */
	public Point getPixelBase() {
		return new Point(x * Grid.GRID_SPACING, y * Grid.GRID_SPACING);
	}
	
	/**
	 * Gets the x tile
	 * @return
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Gets the y tile
	 * @return
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Sets the x
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Sets the y
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Gets a transformed version of the current grid tile combined with arguments
	 * @param x - the amount of x to transform
	 * @param y - the amount of y to transform
	 * @return {GridTile}
	 */
	public GridTile transform(int x, int y) {
		return new GridTile(this.x + x, this.y + y);
	}
	
	/**
	 * Compares a grid tile to another grid tile
	 * @param tile - the Grid tile to compare
	 * @return boolean - true if the same coordinates
	 */
	public boolean compareTo(GridTile tile) {
		return tile != null && tile.getY() == getY() && tile.getX() == getX();
	}
	
	/**
	 * Gets the hitmarker
	 * @return {Hitmarker}
	 */
	public Hitmarker getHitmarker() {
		return hitmarker;
	}

	/**
	 * Sets the hitmarker
	 * @param hitmarker
	 */
	public void setHitmarker(Hitmarker hitmarker) {
		this.hitmarker = hitmarker;
	}
	
	@Override
	public String toString() {
		return "GridTile: [" + this.x + ", " + this.y + "]";
	}
	
	public static class GridTileRectangle {
		
		/**
		 * The southwest and north west grid tiles
		 */
		private GridTile southWest, northEast;
		
		/**
		 * GridTileRectangle
		 * @param southWest
		 * @param northEast
		 */
		public GridTileRectangle(GridTile southWest, GridTile northEast) {
			this.southWest = southWest;
			this.northEast = northEast;
		}

		/**
		 * Gets the south west
		 * @return
		 */
		public GridTile getSouthWest() {
			return southWest;
		}

		/**
		 * Sets the south west
		 * @param southWest
		 */
		public void setSouthWest(GridTile southWest) {
			this.southWest = southWest;
		}

		/**
		 * Gets the north east
		 * @return
		 */
		public GridTile getNorthEast() {
			return northEast;
		}

		/**
		 * Sets the north east
		 * @param northEast
		 */
		public void setNorthEast(GridTile northEast) {
			this.northEast = northEast;
		}
		
	}

}
