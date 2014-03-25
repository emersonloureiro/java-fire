package cf.janga.javafire.eventing.core;

import cf.janga.javafire.annotations.Event;

@Event
public class SampleInvalidEvent {

	public String getKey() {
		return "key";
	}
}
