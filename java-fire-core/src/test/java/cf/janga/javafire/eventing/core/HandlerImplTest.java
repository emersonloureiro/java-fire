/*******************************************************************************
 * Copyright (c) 2014 Emerson Loureiro.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *    Emerson Loureiro - initial API, implementation, and documentation
 *******************************************************************************/
package cf.janga.javafire.eventing.core;

import junit.framework.TestCase;
import cf.janga.javafire.annotations.Handle;
import cf.janga.javafire.eventing.utils.EventingUtils;
import cf.janga.javafire.tests.SampleClassBasedSingleEventListener;
import cf.janga.javafire.tests.SampleEvent;
import cf.janga.javafire.tests.SampleInstanceBasedEventListener;

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
		this.handlerWrapper = new HandlerImpl(handlerWithException, EventingUtils.getHandlerMethods(handlerWithException.getClass()).get(0));
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
		this.handlerWrapper = new HandlerImpl(SampleHandlerWithException.class, EventingUtils.getHandlerMethods(SampleHandlerWithException.class)
				.get(0));
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
