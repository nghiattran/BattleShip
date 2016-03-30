package battleship.client.system.sound;

/**
 * Sound.java
 * @author Nghia Tran
 * @date Mar 6, 2016
 */
public enum Sound {
	
	THEME(0, "theme.wav"),
	SHOOT(1, "shoot.wav"),
	HIT(2, "hit.wav"),
	MISS(3, "miss.wav"),
	WIN(4, "win.wav"),
	LOSE(5, "lose.wav");
	
	/**
	 * The sound id
	 */
	private int id;
	
	/**
	 * For keeping track of sound that is played forever
	 */
	private SoundThread sound;
	
	/**
	 * The file name
	 */
	private String fileName;
	
	/**
	 * Sound
	 * @param id
	 * @param fileName
	 */
	Sound(int id, String fileName) {
		this.id = id;
		this.fileName = fileName;
	}
	
	public String getName() {
		return name().toLowerCase();
	}
	
	/**
	 * play the sound once
	 */
	public void playOnce () {
		final SoundThread once = new SoundThread(fileName);
		once.playOnce();
	}
	
	/**
	 * Play the sound repeatedly
	 */
	public void play() {
		sound = new SoundThread(fileName);
		sound.play();
	}
	
	/**
	 * Stop the sound
	 */
	public void stop() {
		sound.stop();
	}
}
