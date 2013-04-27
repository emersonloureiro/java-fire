package javafire.distribution.core;

import java.util.List;

import javafire.eventing.core.Event;

public interface EventStore {

	void put(List<Event> items) throws EventStoreException;

	Event take() throws EventStoreException;
}
