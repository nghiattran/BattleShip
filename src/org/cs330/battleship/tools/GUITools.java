package org.cs330.battleship.tools;

import java.awt.Point;

import org.cs330.battleship.gui.Application;

/**
 * Tools for the gui
 * GUITools.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class GUITools {
	
	/**
	 * Gets the center screen point
	 * @return {Point}
	 */
	public static Point getCenterScreen() {
		return new Point((int) (Application.SIZE.getWidth() / 2), (int) (Application.SIZE.getHeight() / 2));
	}

}
