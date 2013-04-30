package javafire.distribution.core;

import java.util.List;

import javafire.eventing.core.Event;

/**
 * The store where asynchronous events are kept for processing. The eventing
 * framework will bind to an implementation of this interface at runtime. If
 * more than one implementation is available in this case, the first one found
 * on the classpath will be used.
 * 
 * @author Emerson Loureiro
 */
public interface EventStore {

	/**
	 * Puts the provided events into the store. This operation is an
	 * all-or-nothing one, i.e., either all events are successfully put into the
	 * store, or none is. In the latter case, an {@link EventStoreException} is
	 * thrown.
	 * 
	 * @param events
	 *            The events to be put into the store
	 * @throws EventStoreException
	 *             If there's any underlying error or if not all events are
	 *             successfully put into the store
	 */
	void put(List<Event> events) throws EventStoreException;

	/**
	 * Takes one event from the store.
	 * 
	 * @return {@link Event}
	 * @throws EventStoreException
	 *             If there's any underlying error
	 */
	Event take() throws EventStoreException;
}
