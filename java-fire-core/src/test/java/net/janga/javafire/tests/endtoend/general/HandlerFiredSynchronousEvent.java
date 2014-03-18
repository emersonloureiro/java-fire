package net.janga.javafire.tests.endtoend.general;

import net.janga.javafire.annotations.Event;
import net.janga.javafire.annotations.EventKey;
import net.janga.javafire.annotations.Event.EventType;
import net.janga.javafire.tests.SampleEvent;

@Event(type = EventType.SYNCHRONOUS)
public class HandlerFiredSynchronousEvent extends SampleEvent {

	public HandlerFiredSynchronousEvent(String id) {
		super(id);
	}

	@Override
	@EventKey
	public String getKey() {
		return super.getKey();
	}
}
