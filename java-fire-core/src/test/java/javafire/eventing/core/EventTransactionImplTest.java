package javafire.eventing.core;

import java.util.List;

import javafire.annotations.Event;
import javafire.annotations.Event.DuplicateResolution;
import javafire.annotations.EventKey;
import javafire.distribution.core.EventStore;
import javafire.tests.SampleEvent;
import javafire.tests.SampleSynchronousEvent;
import junit.framework.TestCase;

import org.mockito.Mockito;

public class EventTransactionImplTest extends TestCase {

	private EventTransactionImpl transaction;
	private EventStore originalEventStore;

	@Override
	public void setUp() {
		this.transaction = new EventTransactionImpl();
		this.originalEventStore = DistributedDispatcher.getEventStore();
	}

	@Override
	public void tearDown() {
		DistributedDispatcher.setEventStore(this.originalEventStore);
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
		assertEquals(2, this.transaction.getSynchronousEventQueue().size());
		// Check the asynchronous event queue
		assertEquals(2, this.transaction.getAsynchronousEventQueue().size());
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
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());

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
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());

		try {
			this.transaction.commit();
			fail("TransactionException was expected");
		} catch (TransactionException e) {
		}
		// State of the transaction is the same as when it was
		// rolled back
		assertEquals(4, this.transaction.getFiredEventIds().size());
		assertEquals(0, this.transaction.getSynchronousEventQueue().size());
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());
	}

	public void testCommit() throws Exception {
		EventStore eventStore = Mockito.mock(EventStore.class);
		DistributedDispatcher.setEventStore(eventStore);

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
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());

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
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());

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
		assertEquals(0, this.transaction.getAsynchronousEventQueue().size());
		// Ensure events have been put into the store
		Mockito.verify(eventStore, Mockito.times(1)).put(Mockito.isA(List.class));
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
		assertEquals(1, this.transaction.getAsynchronousEventQueue().size());
		assertSame(event1, this.transaction.getAsynchronousEventQueue().peek());
		// Reset the transaction
		this.transaction = new EventTransactionImpl();
		// Check when the duplicate resolution of the
		// event is "last wins"
		event1 = new EventImpl(new SampleLastWinDuplicateResolutionEvent());
		event2 = new EventImpl(new SampleLastWinDuplicateResolutionEvent());
		this.transaction.eventFired(event1);
		this.transaction.eventFired(event2);
		assertEquals(1, this.transaction.getAsynchronousEventQueue().size());
		assertSame(event2, this.transaction.getAsynchronousEventQueue().peek());
	}

	@Event(duplicateResolution = DuplicateResolution.LAST_WINS)
	@SuppressWarnings("unused")
	private class SampleLastWinDuplicateResolutionEvent {

		@EventKey
		public String getKey() {
			return "key";
		}
	}
}
