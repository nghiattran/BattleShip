package org.cs330.battleship.gui.screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.cs330.battleship.gui.Application;
import org.cs330.battleship.tools.FileUtils;

/**
 * Represents a screen to hold organized panels and components
 * Screen.java
 * @author Clayton Williams
 * @date Feb 7, 2016
 */
public class Screen extends JPanel {
	
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3476544084256420678L;
	
	/**
	 * The background 
	 */
	private BufferedImage background;
	
	/**
	 * The current opacity
	 */
	private float currentOpacity = 0.0f;
	
	/**
	 * If the screen is loaded
	 */
	protected boolean loaded = false;
	
	/**
	 * Screen
	 */
	public Screen() {
		this(false);
	}
	
	/**
	 * Screen
	 * @param skipFade - whether we should skip fading this screen in on load
	 */
	public Screen(boolean skipFade) {
		background = FileUtils.getSprite("./bg.png");
		setPreferredSize(Application.SIZE);
		this.setLocation(0, 0);
		setLayout(null);
		this.setBackground(Color.BLACK);
		if (skipFade) {
			currentOpacity = 1.0f;
			loaded = true;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (currentOpacity < 1.0f) {
        	currentOpacity += 0.01f;
        	if (currentOpacity > 1.0f) {
        		currentOpacity = 1.0f;
        		loaded = true;
        	}
            g2d.setComposite(AlphaComposite.SrcOver.derive(currentOpacity));
        }
        g2d.drawImage(background, 0, 0, null);
	}

}
