package net.janga.javafire.eventing.core;

import net.janga.javafire.annotations.Event;

@Event
public class SampleInvalidEvent {

	public String getKey() {
		return "key";
	}
}
