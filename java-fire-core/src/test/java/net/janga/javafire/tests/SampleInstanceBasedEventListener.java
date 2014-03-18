package net.janga.javafire.tests;

import net.janga.javafire.annotations.Handle;

public class SampleInstanceBasedEventListener {

	public int sampleEventsHandled = 0;

	@Handle
	public void handleSomeEvent(SampleEvent event) {
		this.sampleEventsHandled++;
	}
}
