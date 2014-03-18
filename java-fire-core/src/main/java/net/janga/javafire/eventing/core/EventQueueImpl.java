package net.janga.javafire.eventing.core;

import java.util.LinkedHashMap;
import java.util.Map;


class EventQueueImpl implements EventQueue {

	private final Map<String, Event> queue;

	public EventQueueImpl() {
		this.queue = new LinkedHashMap<String, Event>();
	}

	@Override
	public Event peek() {
		return this.queue.values().iterator().next();
	}

	@Override
	public Event take() {
		Event event = this.queue.values().iterator().next();
		this.queue.remove(event.getId());
		return event;
	}

	@Override
	public void add(Event event) {
		this.queue.put(event.getId(), event);
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

	@Override
	public int size() {
		return this.queue.size();
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
}
