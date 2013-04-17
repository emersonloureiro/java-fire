package javafire.tests;

import javafire.annotations.Event;
import javafire.annotations.EventKey;

@Event
public class SampleEvent {

	private final String key;

	public SampleEvent() {
		this(null);
	}

	public SampleEvent(String key) {
		this.key = key;
	}

	@EventKey
	public String getKey() {
		return this.key;
	}
}
