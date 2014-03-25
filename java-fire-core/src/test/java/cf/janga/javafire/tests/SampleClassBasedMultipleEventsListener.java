package cf.janga.javafire.tests;

import cf.janga.javafire.annotations.Handle;

public class SampleClassBasedMultipleEventsListener {

	public static int sampleEventsHandled = 0;
	public static int sampleSynchronousEventsHandled = 0;

	@Handle
	public void handleSomeEvent(SampleEvent event) {
		sampleEventsHandled++;
	}

	@Handle
	public void handleSomeEvent(SampleSynchronousEvent event) {
		sampleSynchronousEventsHandled++;
	}
}
