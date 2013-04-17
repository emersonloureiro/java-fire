package javafire.distribution.core;

import java.util.List;

public interface EventStore {

	void put(List<EventStoreItem> items) throws EventStoreException;

	EventStoreItem take() throws EventStoreException;

	int size();
}
