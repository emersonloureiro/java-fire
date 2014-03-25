package cf.janga.javafire.tests.endtoend.general;

import cf.janga.javafire.annotations.Handle;
import cf.janga.javafire.eventing.core.EventBus;
import cf.janga.javafire.eventing.core.EventException;
import cf.janga.javafire.tests.SampleSynchronousEvent;

public class EventFiringListener {

	private static int ID = 0;

	public static int sampleSynchronousEventsProcessed = 0;

	@Handle
	public void handleAndFire(SampleSynchronousEvent event) throws EventException {
		sampleSynchronousEventsProcessed++;
		EventBus.fire(new HandlerFiredSynchronousEvent(String.valueOf(ID++)));
	}
}
