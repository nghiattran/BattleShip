package org.cs330.battleship.worker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.cs330.battleship.Constants;
import org.cs330.battleship.gui.Application;

/**
 * The main update thread for our game (the game loop essentially)
 * UpdateWorker.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public final class UpdateWorker implements Runnable {
	
	/**
	 * Our main executor for threadding
	 */
	private final Executor EXECUTOR = Executors.newSingleThreadExecutor();

	/**
	 * The last start time of a cycle.
	 */
	private long lastCycleStart;

	/**
	 * If the major update worker has started.
	 */
	private boolean running;
	
	/**
	 * Starts our game update worker
	 */
	public void startWorker() {
		if (running) {
			return;
		}
		running = true;
		EXECUTOR.execute(UpdateWorker.this);
	}

	@Override
	public void run() {
		while (running) {
			try {
				lastCycleStart = System.currentTimeMillis();
				Application.getInstance().cycle();
				Thread.sleep(1000 / Constants.FRAME_RATE); //todo monitor fps
			} catch (Throwable e) { //throwable for top level tracing
				e.printStackTrace();
			}
		}
		running = false;
	}

}
