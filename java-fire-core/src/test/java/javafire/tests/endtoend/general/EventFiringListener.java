package javafire.tests.endtoend.general;

import javafire.annotations.Handle;
import javafire.eventing.core.EventBus;
import javafire.eventing.core.EventException;
import javafire.tests.SampleSynchronousEvent;

public class EventFiringListener {

	private static int ID = 0;

	public static int sampleSynchronousEventsProcessed = 0;

	@Handle
	public void handleAndFire(SampleSynchronousEvent event) throws EventException {
		sampleSynchronousEventsProcessed++;
		EventBus.fire(new HandlerFiredSynchronousEvent(String.valueOf(ID++)));
	}
}
