package cf.janga.javafire.tests;

import cf.janga.javafire.annotations.Event;
import cf.janga.javafire.annotations.EventKey;
import cf.janga.javafire.annotations.Event.EventType;

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
