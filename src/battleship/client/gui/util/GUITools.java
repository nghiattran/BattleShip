package battleship.client.gui.util;

import java.awt.Point;

import javax.swing.JComponent;

import battleship.client.gui.Application;

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
	
	/**
	 * Gets the center screen point that the given component should be
	 * @param component
	 * @return {Point}
	 */
	public static Point getCenterScreen(JComponent component) {
		return new Point((int) ((Application.SIZE.getWidth() / 2) - component.getWidth() / 2), (int) ((Application.SIZE.getHeight() / 2) - component.getHeight() / 2));
	}
	
	public static Point getCenterScreen(JComponent component, JComponent relativeTo) {
		return new Point((int) ((relativeTo.getWidth() / 2) - component.getWidth() / 2), (int) ((relativeTo.getHeight() / 2) - component.getHeight() / 2));
	}

}
