package org.cs330.battleship.gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.cs330.battleship.Constants;
import org.cs330.battleship.gui.screens.Screen;
import org.cs330.battleship.gui.screens.impl.MainMenu;
import org.cs330.battleship.worker.UpdateWorker;

/**
 * The main application window for our game
 * Application.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public class Application extends JFrame {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = 7068369198933722772L;
	
	/**
	 * The size of our game
	 */
	public static final Dimension SIZE = new Dimension(1280, 720);
	
	/**
	 * A singleton instance of the game application
	 */
	private static Application game;
	
	/**
	 * The update worker
	 */
	private static UpdateWorker UPDATE_WORKER = new UpdateWorker();
	
	/**
	 * The current screen
	 */
	private Screen screen;
	
	/**
	 * Main - start. Invoke later for swing ui
	 */
	public static void main(String... args){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				UPDATE_WORKER.startWorker();		
			}			
		});
	}
	
	/**
	 * Application
	 */
	public Application() {
		init();
	}
	
	/**
	 * Configure and initialize our frame/components
	 */
	private void init() {
		this.setTitle(Constants.TITLE);
		this.setPreferredSize(SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().add(screen = new MainMenu());
		this.validate();
		pack();
		this.setVisible(true);
	}
	
	/**
	 * Changes the screen to another {Screen}
	 * @param screen - the screen
	 */
	public void changeScreen(Screen screen) {
		this.getContentPane().remove(this.screen);
		this.getContentPane().add(this.screen = screen);
		this.revalidate();
	}
	
	/**
	 * Cycles our game then renders
	 */
	public void cycle() {
		getContentPane().repaint();
	}
	
	/**
	 * Gets the application frame
	 * @return {Application}
	 */
	public static Application getInstance() {
		if (game == null) {
			game = new Application();
		}
		return Application.game;
	}

}
