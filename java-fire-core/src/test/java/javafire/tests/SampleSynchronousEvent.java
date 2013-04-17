package javafire.tests;

import javafire.annotations.Event;
import javafire.annotations.Event.EventType;
import javafire.annotations.EventKey;

@Event(type = EventType.SYNCHRONOUS)
public class SampleSynchronousEvent {

	private final String key;

	public SampleSynchronousEvent(String key) {
		this.key = key;
	}

	@EventKey
	public String getKey() {
		return this.key;
	}
}
