package net.janga.javafire.tests;

import net.janga.javafire.annotations.Event;
import net.janga.javafire.annotations.EventKey;
import net.janga.javafire.annotations.Event.EventType;

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
