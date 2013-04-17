package javafire.tests.endtoend.general;

import javafire.annotations.Event;
import javafire.annotations.Event.EventType;
import javafire.annotations.EventKey;
import javafire.tests.SampleEvent;

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
