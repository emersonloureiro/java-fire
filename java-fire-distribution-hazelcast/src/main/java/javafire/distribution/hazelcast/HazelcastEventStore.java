package javafire.distribution.hazelcast;

import java.util.List;
import java.util.Map;

import javafire.distribution.core.EventStore;
import javafire.distribution.core.EventStoreException;
import javafire.eventing.core.Event;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.Transaction;

public class HazelcastEventStore implements EventStore {

	private static final String EVENT_QUEUE_NAME = "hazelcast_event_queue";

	@Override
	public void put(List<Event> events) throws EventStoreException {
		if (!events.isEmpty()) {
			HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
			Transaction transaction = hazelcastInstance.getTransaction();
			try {
				transaction.begin();
				IQueue<EventQueueItem> eventQueue = hazelcastInstance.getQueue(EVENT_QUEUE_NAME);
				for (Event event : events) {
					Map<String, Object> eventProperties = event.getProperties();
					String eventClass = event.getWrappedEvent().getClass().getName();
					eventQueue.put(new EventQueueItem(eventClass, eventProperties));
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				throw new EventStoreException(e);
			}
		}
	}

	@Override
	public Event take() throws EventStoreException {
		return null;
	}

	private class EventQueueItem {
		private final String eventClass;
		private final Map<String, Object> eventProperties;

		public EventQueueItem(String eventClass, Map<String, Object> eventProperties) {
			this.eventClass = eventClass;
			this.eventProperties = eventProperties;
		}
	}
}
