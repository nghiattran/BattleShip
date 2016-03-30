package battleship.server;

import battleship.server.game.GameEngine;
import battleship.server.net.NioReactor;
import battleship.server.system.util.ServerPrintWriter;

/**
 * RunServer.java
 * @author Clayton Williams
 * @date Feb 16, 2016
 */
public class Server {
	
	/**
	 * The {RunServer} instance
	 */
	private static Server INSTANCE;
	
	/**
	 * The engine thread rate (ms)
	 */
	public static final int ENGINE_RATE = 300;
	
	/**
	 * The default port to run on
	 */
	private static final int DEFAULT_PORT = 43500;
	
	/**
	 * The nioreactor
	 */
	private NioReactor reactor;
	
	/**
	 * The game engine
	 */
	private GameEngine gameEngine = new GameEngine();

	/**
	 * Starts the battleship server
	 * @param args
	 */
	public static void main(String[] args) {
		getInstance().startServer(DEFAULT_PORT);
	}
	
	/**
	 * Starts the server on a specified port
	 * @param port - the port to run the server on
	 */
	public void startServer(int port) {
		try {
			System.setOut(new ServerPrintWriter(System.out));
			reactor = NioReactor.configure(43500);
			reactor.start();
			gameEngine.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets if the server is running currently
	 * @return {Boolean} true if running, false if not
	 */
	public boolean isRunning() {
		return reactor != null && reactor.isRunning();
	}
	
	/**
	 * Attempts to terminate the reactor
	 * @return true if terminated
	 */
	public boolean terminate() {
		if (reactor != null && reactor.isRunning()) {
			reactor.terminate();
			gameEngine.setRunning(false);
			return true;
		}
		return false; 
	}
	
	/**
	 * Gets the GameEngine
	 * @return {GameEngine}
	 */
	public GameEngine getGameEngine() {
		return this.gameEngine;
	}
	
	/**
	 * Gets the RunServer instance or creates one
	 * @return {RunServer}
	 */
	public static Server getInstance() {
		if (INSTANCE == null) {
			return INSTANCE = new Server();
		}
		return INSTANCE;
	}
	
}
