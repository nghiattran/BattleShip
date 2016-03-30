package battleship.client.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;

import battleship.client.Constants;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.screen.impl.MainMenu;
import battleship.client.net.Connector;
import battleship.client.system.FileSystem;
import battleship.client.worker.UpdateWorker;

/**
 * The main application window for our game
 * Application.java
 * @author Clayton Williams
 * @author Nghia Tran
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
	 * The connector for networking
	 */
	private Connector connector = new Connector();
	
	/**
	 * Main - start. Invoke later for swing ui
	 */
	public static void main(String... args){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				FileSystem.loadLocal(new File("assets/"));
				Application.getInstance().getConnector().connect();
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
		if (connector != null && connector.isConnected()) {
			connector.parseIncomingPackets();
		}
		getContentPane().repaint();
	}
	
	/**
	 * Gets the connector
	 * @return {Connector}
	 */
	public Connector getConnector() {
		return connector;
	}
	
	/**
	 * Gets the screen
	 * @return {Screen} - the current game screen
	 */
	public Screen getScreen() {
		return screen;
	}
	
	/**
	 * Gets the screen via a class cast
	 * @param classCast - the class to cast
	 * @return {T} or null
	 */
	public <T> T getScreen(Class<T> classCast) {
		try {
	        return classCast.cast(screen);
	    } catch(ClassCastException e) {
	        return null;
	    }
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
