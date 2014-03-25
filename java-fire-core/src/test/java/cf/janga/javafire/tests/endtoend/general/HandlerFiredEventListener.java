package cf.janga.javafire.tests.endtoend.general;

import cf.janga.javafire.annotations.Handle;

public class HandlerFiredEventListener {

	public static int handlerFiredSynchronousEventsProcessed = 0;

	@Handle
	public void handleListenerFiredEvent(HandlerFiredSynchronousEvent event) {
		handlerFiredSynchronousEventsProcessed++;
	}
}
