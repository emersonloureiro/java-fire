package javafire.eventing.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javafire.distribution.core.EventStore;
import javafire.distribution.core.EventStoreException;

class DistributedDispatcher {

	private static EventStore eventStore;

	static {
		ServiceLoader<EventStore> loader = ServiceLoader.load(EventStore.class);
		Iterator<EventStore> eventStoreImplementations = loader.iterator();
		if (eventStoreImplementations.hasNext()) {
			eventStore = eventStoreImplementations.next();
		}
	}

	static void setEventStore(EventStore store) {
		eventStore = store;
	}

	static EventStore getEventStore() {
		return eventStore;
	}

	public static void dispatch(EventQueue eventQueue) throws EventStoreException {
		List<Event> events = new ArrayList<Event>(eventQueue.size());
		while (!eventQueue.isEmpty()) {
			events.add(eventQueue.take());
		}
		eventStore.put(events);
	}
}
