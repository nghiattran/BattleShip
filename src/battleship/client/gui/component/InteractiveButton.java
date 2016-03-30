package battleship.client.gui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import battleship.client.system.FileSystem;

/**
 * Represents an interactive (hovered) JButton
 * InteractiveButton.java
 * @author Clayton Williams
 * @date Feb 23, 2016
 */
public class InteractiveButton extends JButton {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -4862170207406421173L;
	
	/**
	 * InteractiveButton
	 * @param defaultImage
	 * @param hoverImage
	 */
	public InteractiveButton(String defaultImage, String hoverImage) {
		this("", defaultImage, hoverImage);
	}
	
	/**
	 * InteractiveButton
	 * @param text
	 * @param defaultImage
	 * @param hoverImage
	 */
	public InteractiveButton(String text, String defaultImage, String hoverImage) {
		this(text, defaultImage, hoverImage, null);
	}
	
	/**
	 * Button
	 * @param text
	 * @param defaultImage
	 * @param hoverImage
	 * @param location - {Point} of location on screen
	 */
	public InteractiveButton(String text, String defaultImage, String hoverImage, Point location) {
		Image defaultImg = FileSystem.getImage(defaultImage);
		Image hoverImg = FileSystem.getImage(hoverImage);
		this.setText(text);
		this.setLocation(location == null ? new Point(0, 0) : location);
		this.setIcon(new ImageIcon(defaultImg));	
		this.setRolloverIcon(new ImageIcon(hoverImg));
		this.setRolloverEnabled(true);
		this.setVisible(true);
		this.setSize(defaultImg.getWidth(null), defaultImg.getHeight(null));
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.CENTER);
		this.setForeground(Color.WHITE);
		this.setFont(new Font(this.getFont().getName(), Font.BOLD, 12));
	}
	
}
