package battleship.client.gui.structures;

import battleship.client.system.sound.Sound;

/**
 * Represents a hitmarker
 * Hitmarker.java
 * @author Clayton Williams
 * @author Nghia Tran
 * @date Mar 6, 2016
 *
 */
public enum Hitmarker {

	NONE(-1),
	HIT(2),
	MISS(3);
	
	/**
	 * The sound id
	 */
	private int soundId;
	
	/**
	 * Hitmarker
	 * @param soundId
	 */
	Hitmarker(int soundId) {
		this.soundId = soundId;
	}
	
	/**
	 * Play's a sound of this hitmarker
	 */
	public void play() {
		Sound.values()[soundId].playOnce();
	}
}
