package battleship.client.system.sound;

import javax.sound.sampled.AudioInputStream;

import battleship.client.system.FileSystem;

/**
 * @author Nghia
 * Playing sound files without blocking the game
 * Use run() to play the file repeatedly and 
 * runOnce() to play just one time
 */
public class SoundThread implements Runnable{
	private Thread t;
	private boolean flag;
	private volatile boolean execute;
	private String fileName;
	private SoundUtils util;
	
	public SoundThread(String fileName) {
		this.fileName = fileName;
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		t.stop();
		util.stop();
	}
	
	@Override
	public void run() {
		execute = true;
		AudioInputStream stream = null;
		stream = FileSystem.loadWav(fileName);
		util = new SoundUtils();
		while (!Thread.currentThread().isInterrupted() && execute) {
			try {
				util.play(stream);
			} catch(NullPointerException e) {
				if (stream == null) {
					System.out.println("File " + fileName + " does not exist.");
				}
			}
			execute = execute && flag;
        }
	}

	public void playOnce() {
		flag = false;
		if (t == null) {
			t = new Thread (this, fileName);
			t.start ();
		}
	}
	
	public void play() {
		flag = true;
		if (t == null) {
			t = new Thread (this, fileName);
			t.start ();
		}
	}
	
	public void toggleMute() {
		util.toggleMute();
	}
}