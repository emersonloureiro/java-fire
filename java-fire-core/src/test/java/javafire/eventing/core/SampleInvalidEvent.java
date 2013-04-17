package javafire.eventing.core;

import javafire.annotations.Event;

@Event
public class SampleInvalidEvent {

	public String getKey() {
		return "key";
	}
}
