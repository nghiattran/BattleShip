package battleship.client.gui.screen.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import battleship.client.gui.Application;
import battleship.client.gui.component.InteractiveButton;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.structures.Hitmarker;
import battleship.client.gui.structures.ShipType;
import battleship.client.gui.util.GUITools;
import battleship.client.system.FileSystem;
import battleship.client.system.sound.Sound;
import battleship.client.system.sound.SoundThread;

/**
 * Represents the main menu of our game
 * MainMenu.java
 * @author Clayton Williams
 * @author Nghia Tran
 * @date Feb 6, 2016
 */
public class MainMenu extends Screen { 

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -3476544084256420678L;
	
	/**
	 * The find a new game button
	 */
	private static InteractiveButton startButton = new InteractiveButton("Find a new Game", "button.png", "button_hover.png", new Point(0, 0));
	
	/**
	 * The start server button
	 */
	private static InteractiveButton serverButton = new InteractiveButton("Start Server", "button.png", "button_hover.png", new Point(0, 80));
	
	/**
	 * The menu component
	 */
	private JPanel menu = new JPanel();
	
	/**
	 * MainMenu
	 */
	public MainMenu() {
		super();
		
		menu.setSize(226, 400);
		menu.setLocation((int) (GUITools.getCenterScreen().getX() - (menu.getSize().getWidth() / 2)), 220);
		menu.setVisible(false);
		menu.setLayout(null);
		menu.setBackground(null);
		menu.setOpaque(false);
		
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Application.getInstance().changeScreen(new SetupScreen());		
			}
		});
		
		serverButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Application.getInstance().changeScreen(new ServerScreen());		
			}
		});

		menu.add(startButton);
		menu.add(serverButton);
		this.add(menu);
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(FileSystem.getImage("logo.png"), 0, 0, null);
        if (loaded) {
        	if (!menu.isVisible())
        		menu.setVisible(true);
        	g2d.setColor(Color.WHITE);
        	g2d.drawString("Beta - Version 2.3", 15, (int) Application.SIZE.getHeight() - 40);
        }
    }

}
