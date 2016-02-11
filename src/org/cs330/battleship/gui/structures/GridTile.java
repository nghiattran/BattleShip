package org.cs330.battleship.gui.structures;

import java.awt.Point;

import org.cs330.battleship.gui.components.impl.BattleGrid.Grid;

/**
 * GridTile.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class GridTile {
	
	//TODO to char representation for y axis
	
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

}
