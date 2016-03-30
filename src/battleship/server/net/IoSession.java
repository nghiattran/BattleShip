package battleship.server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import battleship.server.net.event.DefaultEventProducer;
import battleship.server.system.PlayerRepository;

/**
 * Represents a connected I/O session.
 * @author Clayton Williams
 */
public class IoSession {
	
	/**
	 * The handshake event producer.
	 */
	private static final EventProducer DEFAULT_PRODUCER = new DefaultEventProducer();

	/**
	 * The selection key.
	 */
	private final SelectionKey key;
	
	/**
	 * The executor service.
	 */
	private final ExecutorService service;
	
	/**
	 * The event producer.
	 */
	private EventProducer producer = DEFAULT_PRODUCER;
	
	/**
	 * The currently queued writing data.
	 */
	private List<ByteBuffer> writingQueue = new ArrayList<>();
	
	/**
	 * The currently queued reading data.
	 */
	private ByteBuffer readingQueue;
	
	/**
	 * The writing lock.
	 */
	private Lock writingLock = new ReentrantLock();
	
	/**
	 * If the session is active.
	 */
	private boolean active = true;
	
	/**
	 * The last ping time stamp.
	 */
	private long lastPing;
	
	/**
	 * The address.
	 */
	private final String address;
	
	/**
	 * Constructs a new {@code IoSession}.
	 * @param key The selection key.
	 * @param service The executor service.
	 */
	public IoSession(SelectionKey key, ExecutorService service) {
		this.key = key;
		this.service = service;
		String address = getRemoteAddress().replaceAll("/", "").split(":")[0];
		this.address = address;
	}

	/**
	 * Fires a write event created using the current event producer.
	 * @param context The event context.
	 */
	public void write(Object context) {
		write(context, false);
	}
	
	/**
	 * Fires a write event created using the current event producer.
	 * @param context The event context.
	 * @param instant If the event should be instantly executed on this thread.
	 */
	public void write(Object context, boolean instant) {
		if (context == null) {
			throw new IllegalStateException("Invalid writing context!");
		}
		if (instant) {
			producer.produceWriter(this, context).run();
			return;
		}
		service.execute(producer.produceWriter(this, context));
	}
	
	/**
	 * Sends the packet data (without write event encoding).
	 * @param buffer The buffer.
	 */
	public void queue(ByteBuffer buffer) {
		writingLock.lock();
		writingQueue.add(buffer);
		writingLock.unlock();
		write();
	}
	
	/**
	 * Handles the writing of all buffers in the queue.
	 */
	public void write() {
		if (!key.isValid()) {
			disconnect();
			return;
		}
		writingLock.lock();
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			while (!writingQueue.isEmpty()) {
				ByteBuffer buffer = writingQueue.get(0);
				channel.write(buffer);
				if (buffer.hasRemaining()) {
					key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
					key.selector().wakeup();
					break;
				}
				writingQueue.remove(0);
			}
		} catch (IOException e) {
			disconnect();
			e.printStackTrace();
		}
		writingLock.unlock();
	}

	/**
	 * Disconnects the session.
	 */
	public void disconnect() {
		try {
			if (!active) {
				return;
			}
			PlayerRepository.removePlayer(key);
			active = false;
			key.cancel();
			SocketChannel channel = (SocketChannel) key.channel();
			channel.socket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the IP-address (without the port).
	 * @return The address.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Gets the remote address of this session.
	 * @return The remote address, as a String.
	 */
	public String getRemoteAddress() {
		try {
			return ((SocketChannel) key.channel()).getRemoteAddress().toString();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Gets the current event producer.
	 * @return The producer.
	 */
	public EventProducer getProducer() {
		return producer;
	}

	/**
	 * Sets the event producer.
	 * @param producer The producer to set.
	 */
	public void setProducer(EventProducer producer) {
		this.producer = producer;
	}

	/**
	 * Gets the queued reading data.
	 * @return The readingQueue.
	 */
	public ByteBuffer getReadingQueue() {
		synchronized (this) {
			return readingQueue;
		}
	}

	/**
	 * Queues reading data.
	 * @param readingQueue The readingQueue to set.
	 */
	public void setReadingQueue(ByteBuffer readingQueue) {
		synchronized (this) {
			this.readingQueue = readingQueue;
		}
	}
	
	/**
	 * Gets the writing lock.
	 * @return The writing lock.
	 */
	public Lock getWritingLock() {
		return writingLock;
	}

	/**
	 * Gets the selection key.
	 * @return The selection key.
	 */
	public SelectionKey getKey() {
		return key;
	}

	/**
	 * @return The active.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Gets the lastPing.
	 * @return The lastPing.
	 */
	public long getLastPing() {
		return lastPing;
	}

	/**
	 * Sets the lastPing.
	 * @param lastPing The lastPing to set.
	 */
	public void setLastPing(long lastPing) {
		this.lastPing = lastPing;
	}

	/**
	 * Gets the writing queue.
	 * @return The writing queue.
	 */
	public List<ByteBuffer> getWritingQueue() {
		return writingQueue;
	}

}