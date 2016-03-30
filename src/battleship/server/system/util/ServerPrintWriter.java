package battleship.server.system.util;

import java.io.PrintStream;

import battleship.client.gui.Application;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.screen.impl.ServerScreen;

/**
 * The print writer
 * ServerPrintWriter.java
 * @author Clayton Williams
 * @date Feb 21, 2016
 */
public class ServerPrintWriter extends PrintStream {

	/**
	 * ServerPrintWriter
	 * @param stream
	 */
	public ServerPrintWriter(PrintStream stream) {
		super(stream);
	}
	
	@Override
	public void println(String message) {
		log(message);
	}

	@Override
	public PrintStream printf(String message, Object... objects) {
		return null;
	}

	@Override
	public void println(boolean message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(int message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(double message) {
		log(String.valueOf(message));
	}

	@Override
	public void println(char message) {
		log(Character.toString(message));
	}

	@Override
	public void println(long message) {
		log(Long.toString(message));
	}

	@Override
	public void println(Object message) {
		log(String.valueOf(message));
	}
	
	/**
	 * Logs the message to the console
	 * @param message
	 */
	private void log(String message) {
		Screen screen = Application.getInstance().getScreen();
		if (screen != null && screen instanceof ServerScreen) {
			((ServerScreen) screen).logConsole(message);
		} else {
			super.println(message);
		}
	}
}
