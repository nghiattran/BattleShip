package battleship.client.net;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import battleship.client.gui.Application;
import battleship.client.gui.component.impl.BattleGrid;
import battleship.client.gui.component.impl.Ship;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.screen.impl.EndGame;
import battleship.client.gui.screen.impl.GameScreen;
import battleship.client.gui.screen.impl.SetupScreen;
import battleship.client.gui.structures.Hitmarker;
import battleship.client.gui.structures.Orientation;
import battleship.client.gui.structures.ShipType;

/**
 * Connector.java - holds a connection to the server
 * @author Clayton Williams
 * @date Feb 18, 2016
 */
public class Connector {
	
	/**
	 * The connection timeout (in milliseconds)
	 */
	private static final int CONNECTION_TIMEOUT = 10_000;
	
	/**
	 * Our socket
	 */
	private Socket socket;
	
	/**
	 * The input stream
	 */
	private DataInputStream input;
	
	/**
	 * The output stream
	 */
	private DataOutputStream output;
	
	/**
	 * Connects to localhost
	 */
	public void connect() {
		connect("192.99.144.144");
	}
	
	/**
	 * Connects to the host
	 */
	public void connect(final String IP) {
		if (isConnected())
			return;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, 43500), CONNECTION_TIMEOUT);
			output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			input = new DataInputStream(socket.getInputStream());
			ByteBuffer b = ByteBuffer.allocate(1).put((byte) 0);
			b.flip();
			write(b);
		} catch (ConnectException e) {
			System.err.println("Connection Refused/Timeout.");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * Writes to the output stream
	 * @param opcode
	 * @param buffer
	 */
	public void write(ByteBuffer buffer) {
		if (isConnected() && output != null) {
			try {
				output.write(buffer.array());
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks & parses the input stream for incoming packet data
	 */
	public void parseIncomingPackets() {
		if (input == null)
			return;
		try {
			if (input.available() > 0) {
				Screen screen = Application.getInstance().getScreen();
				int opcode = input.readByte();			
				switch (opcode) {
					case 1:
						if (screen instanceof SetupScreen) {
							SetupScreen setupScreen = (SetupScreen) screen;
							setupScreen.getSetup().setVisible(false);
							setupScreen.setElapsedTime(0L);
							setupScreen.getCancelSearch().setVisible(true);
						}					
						break;
					case 2:
						BattleGrid battleGrid = ((SetupScreen) screen).getBattleGrid();
						Application.getInstance().changeScreen(new GameScreen(battleGrid));
						for (int i = 0; i < ShipType.values().length; i++) {
							Ship ship = new Ship(ShipType.values()[input.readByte()]);
							int x = input.readByte();
							int y = input.readByte();
							System.out.println(" sdf  " + x + " " + y);
							ship.setLocation(x, y);
							ship.setOrientation(input.readByte() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
							Application.getInstance().getScreen(GameScreen.class).getEnemyGrid().getGrid().getShips()[ship.getShipType().ordinal()] = ship;

						}
						break;
					case 3: //set active turn
					case 4: //register hit marker
						if (screen instanceof GameScreen) {
							GameScreen gs = (GameScreen) screen;
							switch (opcode) {
								case 3:
									boolean turn = input.readByte() == 1;
									gs.setActiveTurn(turn);									
									break;
								case 4:
									int x = input.readByte();
									int y = input.readByte();
									int marker = input.readByte();
									System.out.println("Read byte" + marker);
									Hitmarker hitmarker = Hitmarker.values()[marker];
									hitmarker.play();
									boolean enemyGrid = input.readByte() == 1;
									if (enemyGrid) {
										gs.getEnemyGrid().getGrid().getTiles()[x][y].setHitmarker(hitmarker);
									} else {
										gs.getPlayerGrid().getGrid().getTiles()[x][y].setHitmarker(hitmarker);
									}
									break;
							}
						}
						break;		
					case 9: //end game screen
						boolean won = input.readByte() == 1;
						if (Application.getInstance().getScreen(GameScreen.class) != null) {
							Application.getInstance().changeScreen(new EndGame(won));		
						}						
						break;
					default:
						System.out.println("Unhandled client opcode " + opcode);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the socket is connected
	 * @return true if connected
	 */
	public boolean isConnected() {
		return socket != null && (!socket.isClosed() || socket.isConnected());
	}
	
}
