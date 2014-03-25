package cf.janga.javafire.tests.endtoend.general;

import cf.janga.javafire.annotations.Event;
import cf.janga.javafire.annotations.EventKey;
import cf.janga.javafire.annotations.Event.EventType;
import cf.janga.javafire.tests.SampleEvent;

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
