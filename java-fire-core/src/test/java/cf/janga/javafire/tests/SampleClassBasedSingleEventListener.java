package cf.janga.javafire.tests;

import cf.janga.javafire.annotations.Handle;

public class SampleClassBasedSingleEventListener {

	public static int sampleEventsHandled = 0;

	@Handle
	public void handleSomeEvent(SampleEvent event) {
		sampleEventsHandled++;
	}
}
