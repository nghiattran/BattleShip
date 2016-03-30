package battleship.server.net.event;

import java.nio.ByteBuffer;

import battleship.server.net.EventProducer;
import battleship.server.net.IoReadEvent;
import battleship.server.net.IoSession;
import battleship.server.net.IoWriteEvent;

/**
 * Produces I/O events for default protocols
 * @author Clayton Williams
 */
public final class DefaultEventProducer implements EventProducer {

	@Override
	public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
		return new DefaultReadEvent(session, buffer);
	}

	@Override
	public IoWriteEvent produceWriter(IoSession session, Object context) {
		return new DefaultWriteEvent(session, context);
	}
	
}