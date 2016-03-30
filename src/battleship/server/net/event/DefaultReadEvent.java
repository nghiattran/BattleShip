package battleship.server.net.event;

import java.nio.ByteBuffer;

import battleship.client.gui.component.impl.Ship;
import battleship.client.gui.structures.Orientation;
import battleship.client.gui.structures.ShipType;
import battleship.server.Server;
import battleship.server.game.GameInstance;
import battleship.server.game.player.Player;
import battleship.server.net.IoReadEvent;
import battleship.server.net.IoSession;
import battleship.server.system.PlayerRepository;

/**
 * Handles handshake read events.
 * @author Clayton Williams
 */
public final class DefaultReadEvent extends IoReadEvent {
	
	/**
	 * Constructs a new {@code HSReadEvent}.
	 * @param session The session.
	 * @param buffer The buffer.
	 */
	public DefaultReadEvent(IoSession session, ByteBuffer buffer) {
		super(session, buffer);
	}

	@Override
	public void read(IoSession session, ByteBuffer buffer) {
		final int opcode = buffer.get() & 0xFF;
		System.out.println("Opcode: " + opcode);
		final Player player = PlayerRepository.getPlayer(session.getKey());
		final GameInstance gi = Server.getInstance().getGameEngine().getGame(session);
		switch (opcode) {	
			case 0: //register player
				PlayerRepository.addPlayer(new Player(session));
				break;	
			case 1: //register ship layout, request opponent
				if (player != null) {
					for (int i = 0; i < ShipType.values().length; i++) {
						Ship ship = new Ship(ShipType.values()[buffer.get()]);
						ship.setLocation(buffer.get(), buffer.get());
						ship.setOrientation(buffer.get() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
						player.getShips()[i] = ship;
					}
					player.setQueued(true);
					ByteBuffer b = ByteBuffer.allocate(1);
					b.put((byte) 1);
					session.write(b);
				}
				break;
			case 2: //send hit request
				int x = buffer.get();
				int y = buffer.get();
				if (gi != null) {
					gi.hit(x, y);
				}
				break;
			case 3: //cancel request opponent
				if (player != null) {
					player.setQueued(false);
				}
				break;
			case 4: //forfeit
				if (gi != null && player != null) {
					gi.forfeit(player);
				}
				break;
			default:
				System.out.println("Unhandled opcode: " + opcode + ".");
				session.disconnect();
				break;
		}
		if (buffer.hasRemaining())
			buffer.clear();
	}
	
}