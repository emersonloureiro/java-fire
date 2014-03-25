package cf.janga.javafire.eventing.core;

import cf.janga.javafire.annotations.Handle;
import cf.janga.javafire.eventing.core.EventException;
import cf.janga.javafire.eventing.core.EventImpl;
import cf.janga.javafire.eventing.core.HandlerImpl;
import cf.janga.javafire.eventing.utils.EventingUtils;
import cf.janga.javafire.tests.SampleClassBasedSingleEventListener;
import cf.janga.javafire.tests.SampleEvent;
import cf.janga.javafire.tests.SampleInstanceBasedEventListener;
import junit.framework.TestCase;

public class HandlerImplTest extends TestCase {

	private HandlerImpl handlerWrapper;

	@Override
	public void setUp() {
		// Reset this counter to get the right number
		// of events processed per test run
		SampleClassBasedSingleEventListener.sampleEventsHandled = 0;
	}

	public void testHandle_InstanceProvided() throws Exception {
		SampleInstanceBasedEventListener handler = new SampleInstanceBasedEventListener();
		this.handlerWrapper = new HandlerImpl(handler, EventingUtils.getHandlerMethods(handler.getClass()).get(0));
		// Mimic the handler processing two events
		this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
		this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
		assertEquals(2, handler.sampleEventsHandled);
		// Check when an exception is thrown when handling
		// the event
		SampleHandlerWithException handlerWithException = new SampleHandlerWithException();
		this.handlerWrapper = new HandlerImpl(handlerWithException, EventingUtils.getHandlerMethods(
				handlerWithException.getClass()).get(0));
		try {
			this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
			fail("EventException was expected");
		} catch (EventException e) {
		}
	}

	public void testHandle_ClassProvided() throws Exception {
		this.handlerWrapper = new HandlerImpl(SampleClassBasedSingleEventListener.class, EventingUtils.getHandlerMethods(
				SampleClassBasedSingleEventListener.class).get(0));
		// Mimic the handler processing two events
		this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
		this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
		assertEquals(2, SampleClassBasedSingleEventListener.sampleEventsHandled);
		// Check when an exception is thrown when handling
		// the event
		this.handlerWrapper = new HandlerImpl(SampleHandlerWithException.class, EventingUtils.getHandlerMethods(
				SampleHandlerWithException.class).get(0));
		try {
			this.handlerWrapper.handle(new EventImpl(new SampleEvent()));
			fail("EventException was expected");
		} catch (EventException e) {
		}
	}

	private class SampleHandlerWithException {
		@Handle
		public void handleEventWithException(SampleEvent event) {
			throw new RuntimeException("Booooom!");
		}
	}
}
