package battleship.server.net.event;

import java.nio.ByteBuffer;

import battleship.server.net.IoSession;
import battleship.server.net.IoWriteEvent;
 
/**
 * Handles write events
 * @author Clayton Williams
 */
public final class DefaultWriteEvent extends IoWriteEvent {
	
	/**
	 * Constructs a new {DefaultWriteEvent} {Object}.
	 * @param session The session.
	 * @param context The context.
	 */
	public DefaultWriteEvent(IoSession session, Object context) {
		super(session, context);
	}

	@Override
	public void write(IoSession session, Object context) {
		ByteBuffer buffer = (ByteBuffer) context;
		buffer.flip();
		session.queue(buffer);
	}

}