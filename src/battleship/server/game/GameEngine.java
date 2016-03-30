package battleship.server.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import battleship.server.Server;
import battleship.server.game.player.Player;
import battleship.server.net.IoSession;
import battleship.server.system.PlayerRepository;

/**
 * The game engine that the server runs on
 * GameEngine.java
 * @author Clayton Williams
 * @date Mar 6, 2016
 *
 */
public class GameEngine implements Runnable {
	
	/**
	 * The executor for threads
	 */
	private ExecutorService executor;
	
	/**
	 * If the game engine is running
	 */
	private boolean running = false;
	
	/**
	 * A container of all current games going on
	 */
	private List<GameInstance> games = new ArrayList<GameInstance>();
	
	/**
	 * Starts the game engine
	 */
	public void start() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.execute(this);
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {		
				/** Pulse current games **/
				Iterator<GameInstance> it = games.iterator();
				while(it.hasNext()) {
					GameInstance gi = it.next();
					gi.pulse();
				}
				/** Setup new games for queued players **/
				List<Player> queuedPlayers = PlayerRepository.getQueuedPlayers();
				if (queuedPlayers.size() > 1) {
					Iterator<Player> it2 = queuedPlayers.iterator();
					while(it2.hasNext()) {
						Player p = it2.next();
						if (it2.hasNext()) {
							Player p2 = it2.next();
							registerNewGame(p, p2);
						}
					}
				}
				Thread.sleep(Server.ENGINE_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Gets a game from a session
	 * @param session
	 * @return
	 */
	public GameInstance getGame(IoSession session) {
		Iterator<GameInstance> it = games.iterator();
		while(it.hasNext()) {
			GameInstance gi = it.next();
			if (gi.containsPlayer(PlayerRepository.getPlayer(session.getKey()))) {
				return gi;
			}
		}
		return null;
	}
	
	/**
	 * Registers a new game instance
	 * @param p1 - player 1
	 * @param p2 - player 2
	 */
	private void registerNewGame(Player p1, Player p2) {
		games.add(new GameInstance(p1, p2));
	}
	
	/**
	 * Deregisters a game
	 * @param gi
	 */
	public void deregisterGame(GameInstance gi) {
		Iterator<GameInstance> it = games.iterator();
		while(it.hasNext()) {
			GameInstance gmi = it.next();
			if (gmi.equals(gi)) {
				it.remove();
				return;
			}
		}
	}
	
	/**
	 * Sets the running 
	 * @param running
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

}
