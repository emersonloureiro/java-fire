package net.janga.javafire.tests.endtoend.general;

import net.janga.javafire.annotations.Handle;
import net.janga.javafire.eventing.core.EventBus;
import net.janga.javafire.eventing.core.EventException;
import net.janga.javafire.tests.SampleSynchronousEvent;

public class EventFiringListener {

	private static int ID = 0;

	public static int sampleSynchronousEventsProcessed = 0;

	@Handle
	public void handleAndFire(SampleSynchronousEvent event) throws EventException {
		sampleSynchronousEventsProcessed++;
		EventBus.fire(new HandlerFiredSynchronousEvent(String.valueOf(ID++)));
	}
}
