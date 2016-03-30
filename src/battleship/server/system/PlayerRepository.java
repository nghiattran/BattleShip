package battleship.server.system;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import battleship.server.game.player.Player;

/**
 * Represents a repository for all the connected player sessions
 * PlayerRepository.java
 * @author Clayton Williams
 * @date Mar 5, 2016
 */
public class PlayerRepository {
	
	/**
	 * The connected players
	 */
	private static List<Player> players = new ArrayList<Player>();
	
	/**
	 * Returns a list of queued players
	 * @return {List<Player>}
	 */
	public static List<Player> getQueuedPlayers() {
		List<Player> queued = new ArrayList<Player>();
		Iterator<Player> it = players.iterator();
		while(it.hasNext()) {
			Player p = it.next();
			if (p.isQueued())
				queued.add(p);
		}
		Collections.shuffle(queued);
		return queued;
	}
	
	/**
	 * Gets a player via a SelectionKey
	 * @param key - the selection key
	 * @return {Player}
	 */
	public static Player getPlayer(SelectionKey key) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getSession().getKey().equals(key)) {
				return players.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Adds a player to the repository
	 * @param player - the player to add
	 * @return true if successful
	 */
	public static boolean addPlayer(Player player) {
		return !playerExists(player) && players.add(player);
	}
	
	/**
	 * Removes a player from the repository
	 * @param player
	 * @return
	 */
	public static boolean removePlayer(Player player) {
		return players.remove(player);
	}
	
	/**
	 * Removes a player using a selection key
	 * @param key
	 * @return
	 */
	public static boolean removePlayer(SelectionKey key) {
		Iterator<Player> it = players.iterator();
		while(it.hasNext()) {		
			Player player = it.next();
			if (player.getSession().getKey().equals(key)) {
				player.disconnect();
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a player exists
	 * @param player
	 * @return
	 */
	private static boolean playerExists(Player player) {
		for(Player p : players) {
			if (p.equals(player)) {
				return true;
			}
		}
		return false;
	}

}
