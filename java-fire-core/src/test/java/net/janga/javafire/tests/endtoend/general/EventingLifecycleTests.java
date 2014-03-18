package net.janga.javafire.tests.endtoend.general;

import net.janga.javafire.eventing.core.BaseEventingTest;
import net.janga.javafire.eventing.core.EventBus;
import net.janga.javafire.eventing.core.EventTransaction;
import net.janga.javafire.tests.SampleClassBasedMultipleEventsListener;
import net.janga.javafire.tests.SampleSynchronousEvent;

/**
 * This covers tests related to all eventing life cycles (e.g., start
 * transaction, fire events, rollback).
 * 
 * @author Emerson Loureiro
 * 
 */
public class EventingLifecycleTests extends BaseEventingTest {

	public void testRegistrationFiringRollback_SynchronousEvents() throws Exception {
		// Register listeners
		EventBus.register(SampleClassBasedMultipleEventsListener.class);
		// Start by firing a couple of events, transaction
		// should be automatically started.
		EventBus.fire(new SampleSynchronousEvent("1"));
		EventBus.fire(new SampleSynchronousEvent("2"));
		EventBus.fire(new SampleSynchronousEvent("3"));
		EventBus.fire(new SampleSynchronousEvent("4"));
		EventBus.fire(new SampleSynchronousEvent("5"));
		// Now rollback the transaction
		EventTransaction firstTransaction = EventBus.getCurrentTransaction();
		firstTransaction.rollback();
		assertTrue(firstTransaction.isRolledBack());
		// Switch this off to prevent another transaction
		// starting
		EventBus.setAutomaticTransactionStart(false);
		assertNull(EventBus.getCurrentTransaction());
		EventBus.setAutomaticTransactionStart(true);
		assertEquals(0, SampleClassBasedMultipleEventsListener.sampleEventsHandled);

		// Start all over again, firing new events,
		// this will test both a case where multiple
		// transactions are started and rolled back or
		// committed within the same thread, as it is
		// with thread pools
		EventBus.fire(new SampleSynchronousEvent("1"));
		EventBus.fire(new SampleSynchronousEvent("2"));
		EventBus.fire(new SampleSynchronousEvent("3"));
		EventBus.fire(new SampleSynchronousEvent("4"));
		EventBus.fire(new SampleSynchronousEvent("5"));
		// Rollback the transaction again
		EventTransaction secondTransaction = EventBus.getCurrentTransaction();
		secondTransaction.rollback();
		assertTrue(secondTransaction.isRolledBack());
		// Switch this off to prevent another transaction
		// starting
		EventBus.setAutomaticTransactionStart(false);
		assertNull(EventBus.getCurrentTransaction());
		EventBus.setAutomaticTransactionStart(true);
		assertEquals(0, SampleClassBasedMultipleEventsListener.sampleEventsHandled);
		// Ensure both transactions are different
		assertNotSame(firstTransaction, secondTransaction);
	}

	public void testRegistrationFiringCommit_SynchronousEvents() throws Exception {
		// Register listeners
		EventBus.register(SampleClassBasedMultipleEventsListener.class);
		// Start by firing a couple of events, transaction
		// should be automatically started.
		EventBus.fire(new SampleSynchronousEvent("1"));
		EventBus.fire(new SampleSynchronousEvent("2"));
		EventBus.fire(new SampleSynchronousEvent("3"));
		EventBus.fire(new SampleSynchronousEvent("4"));
		EventBus.fire(new SampleSynchronousEvent("5"));
		// Ensure nothing's been fired until the commit
		assertEquals(0, SampleClassBasedMultipleEventsListener.sampleSynchronousEventsHandled);
		// Now commit the transaction
		EventTransaction firstTransaction = EventBus.getCurrentTransaction();
		firstTransaction.commit();
		assertTrue(firstTransaction.isCommitted());
		// Switch this off temporarily to prevent another transaction
		// starting
		EventBus.setAutomaticTransactionStart(false);
		assertNull(EventBus.getCurrentTransaction());
		EventBus.setAutomaticTransactionStart(true);
		assertEquals(5, SampleClassBasedMultipleEventsListener.sampleSynchronousEventsHandled);

		// Start all over again, firing new events,
		// this will test both a case where multiple
		// transactions are started and rolled back or
		// committed within the same thread, as it is
		// with thread pools
		EventBus.fire(new SampleSynchronousEvent("1"));
		EventBus.fire(new SampleSynchronousEvent("2"));
		EventBus.fire(new SampleSynchronousEvent("3"));
		EventBus.fire(new SampleSynchronousEvent("4"));
		EventBus.fire(new SampleSynchronousEvent("5"));
		// Ensure nothing's been fired until the commit
		assertEquals(5, SampleClassBasedMultipleEventsListener.sampleSynchronousEventsHandled);
		// Now commit the transaction
		EventTransaction secondTransaction = EventBus.getCurrentTransaction();
		secondTransaction.commit();
		assertTrue(secondTransaction.isCommitted());
		// Switch this off to prevent another transaction
		// starting
		EventBus.setAutomaticTransactionStart(false);
		assertNull(EventBus.getCurrentTransaction());
		EventBus.setAutomaticTransactionStart(true);
		assertEquals(10, SampleClassBasedMultipleEventsListener.sampleSynchronousEventsHandled);
		// Ensure both transactions are different
		assertNotSame(firstTransaction, secondTransaction);
	}

	/**
	 * This tests the scenario where handlers process an event, and as
	 * consequence of that, more events are fired, all within the same
	 * transaction.
	 */
	public void testChainedFiringEvents() throws Exception {
		EventBus.register(EventFiringListener.class);
		EventBus.register(HandlerFiredEventListener.class);
		// Fire some events
		EventBus.fire(new SampleSynchronousEvent("1"));
		EventBus.fire(new SampleSynchronousEvent("2"));
		// Now commit the transaction
		EventBus.getCurrentTransaction().commit();
		// Assert events were processed as expected. One of
		// the listeners will process two events, thus firing
		// two other events, picked up by another listener.
		assertEquals(2, EventFiringListener.sampleSynchronousEventsProcessed);
		assertEquals(2, HandlerFiredEventListener.handlerFiredSynchronousEventsProcessed);
	}
}
