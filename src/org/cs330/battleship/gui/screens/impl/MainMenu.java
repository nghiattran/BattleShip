package org.cs330.battleship.gui.screens.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import org.cs330.battleship.gui.Application;
import org.cs330.battleship.gui.screens.Screen;
import org.cs330.battleship.tools.FileUtils;
import org.cs330.battleship.tools.GUITools;

/**
 * Represents the main menu of our game
 * MainMenu.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public class MainMenu extends Screen {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3476544084256420678L;

	/**
	 * The JButton
	 */
	private JButton startButton = new JButton("Find New Game");
	
	/**
	 * MainMenu
	 */
	public MainMenu() {
		super();		
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Application.getInstance().changeScreen(new SetupScreen());		
			}
		});
		startButton.setSize(200, 50);
		startButton.setLocation((int) (GUITools.getCenterScreen().getX() - (startButton.getSize().getWidth() / 2)), 210);
		startButton.setVisible(false);
		this.add(startButton);		
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(FileUtils.getSprite("logo.png"), 0, 0, null);
     
        if (loaded || true) {
        	if (!startButton.isVisible())
        		startButton.setVisible(true);
        	g2d.setColor(Color.WHITE);
        	g2d.drawString("Alpha - Version 1.0", 15, (int) Application.SIZE.getHeight() - 40);
        }
    }

}
