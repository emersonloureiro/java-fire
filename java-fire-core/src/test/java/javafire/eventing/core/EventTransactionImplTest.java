package javafire.eventing.core;

import javafire.annotations.Event;
import javafire.annotations.Event.DuplicateResolution;
import javafire.annotations.EventKey;
import javafire.tests.SampleEvent;
import javafire.tests.SampleSynchronousEvent;
import junit.framework.TestCase;

public class EventTransactionImplTest extends TestCase {

	private EventTransactionImpl transaction;

	@Override
	public void setUp() {
		this.transaction = new EventTransactionImpl();
	}

	@Override
	public void tearDown() {
	}

	/**
	 * Tests the basics of delivering fired events to the transaction.
	 */
	public void testEventFired() throws Exception {
		this.transaction.eventFired(new EventImpl(new SampleEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleEvent("2")));
		this.transaction.eventFired(new EventImpl(new SampleEvent("2")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("2")));
		// One of the events is a duplicate so it won't be added to
		// the count
		assertEquals(4, this.transaction.getFiredEventIds().size());
		// Check the synchronous event queue
		assertEquals(4, this.transaction.getSynchronousEventQueue().size());
	}

	public void testRollback() throws Exception {
		// Fire some events and rollback the transaction
		this.transaction.eventFired(new EventImpl(new SampleEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleEvent("2")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("2")));
		this.transaction.rollback();
		// List of fired events doesn't get cleared up,
		// history is kept there regardless
		assertEquals(4, this.transaction.getFiredEventIds().size());
		// Both event queues are cleared up
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());

		try {
			// Attempting to fire more events to the transaction
			// should result in an error. Given it's been rolled
			// back, it's now at an invalid state
			this.transaction.eventFired(new EventImpl(new SampleEvent("3")));
			fail("TransactionException was expected");
		} catch (TransactionException e) {
		}
		// State of the transaction is the same as when it was
		// rolled back
		assertEquals(4, this.transaction.getFiredEventIds().size());
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());

		try {
			this.transaction.commit();
			fail("TransactionException was expected");
		} catch (TransactionException e) {
		}
		// State of the transaction is the same as when it was
		// rolled back
		assertEquals(4, this.transaction.getFiredEventIds().size());
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());
	}

	public void testCommit() throws Exception {
		// Fire some events and commit the transaction
		this.transaction.eventFired(new EventImpl(new SampleEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleEvent("2")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("1")));
		this.transaction.eventFired(new EventImpl(new SampleSynchronousEvent("2")));
		this.transaction.commit();
		// List of fired events doesn't get cleared up,
		// history is kept there regardless
		assertEquals(4, this.transaction.getFiredEventIds().size());
		// Both event queues are cleared up
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());

		try {
			// Attempting to fire more events to the transaction
			// should result in an error. Given it's been committed,
			// it's now at an invalid state
			this.transaction.eventFired(new EventImpl(new SampleEvent("3")));
			fail("TransactionException was expected");
		} catch (TransactionException e) {
		}
		// State of the transaction is the same as when it was
		// rolled back
		assertEquals(4, this.transaction.getFiredEventIds().size());
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());

		try {
			// It's wrong to commit a transaction again!
			this.transaction.commit();
			fail("TransactionException was expected");
		} catch (TransactionException e) {
		}
		// State of the transaction is the same as when it was
		// rolled back
		assertEquals(4, this.transaction.getFiredEventIds().size());
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());
	}

	/**
	 * Tests the firing of events within the transaction with regards to
	 * duplicate events resolution.
	 */
	public void testEventFired_DuplicateResolution() throws Exception {
		// Check when the duplicate resolution of the
		// event is "first wins"
		javafire.eventing.core.Event event1 = new EventImpl(new SampleEvent("1"));
		javafire.eventing.core.Event event2 = new EventImpl(new SampleEvent("1"));
		this.transaction.eventFired(event1);
		this.transaction.eventFired(event2);
		// Reset the transaction
		this.transaction = new EventTransactionImpl();
		// Check when the duplicate resolution of the
		// event is "last wins"
		event1 = new EventImpl(new SampleLastWinDuplicateResolutionEvent());
		event2 = new EventImpl(new SampleLastWinDuplicateResolutionEvent());
		this.transaction.eventFired(event1);
		this.transaction.eventFired(event2);
	}

	@Event(duplicateResolution = DuplicateResolution.LAST_WINS)
	private class SampleLastWinDuplicateResolutionEvent {

		@EventKey
		public String getKey() {
			return "key";
		}
	}
}
