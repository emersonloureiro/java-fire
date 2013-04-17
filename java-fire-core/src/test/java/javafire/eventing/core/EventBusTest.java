package javafire.eventing.core;

import java.util.List;

import javafire.tests.SampleEvent;
import javafire.tests.SampleClassBasedSingleEventListener;

public class EventBusTest extends BaseEventingTest {

	public void testGetCurrentTransaction() {
		// When the automatic transaction start property
		// is enabled, attempting to get the current transaction
		// should create one if there's none.
		EventTransaction transaction1 = EventBus.getCurrentTransaction();
		assertNotNull(transaction1);
		// Given we're not mocking out the creation of the
		// transaction, it should be one with a valid id
		assertNotNull(transaction1.getId());

		// Manually clear the transaction, as it's a thread local
		EventBus.setCurrentTransaction(null);

		// Set the property to false, so no transaction
		// should be created until explicitly requested
		EventBus.setAutomaticTransactionStart(false);
		assertNull(EventBus.getCurrentTransaction());

		// Set the property back to true, and new transactions
		// should be created now
		EventBus.setAutomaticTransactionStart(true);
		EventTransaction transaction2 = EventBus.getCurrentTransaction();
		assertNotNull(transaction2);
		assertNotSame(transaction1, transaction2);

		// Now get the current transaction, again
		// without having reset. Should be the same
		// as the previously created one
		assertSame(transaction2, EventBus.getCurrentTransaction());
	}

	public void testStartTransaction() {
		// Manually start a transaction, with the
		// automatic transaction start property set to true,
		// which should create a transaction
		EventTransaction transaction1 = EventBus.startTransaction();
		assertNotNull(transaction1);
		assertSame(transaction1, EventBus.getCurrentTransaction());

		// Attempting to start a transaction again within
		// the same thread, shouldn't do anything; the same
		// existing transaction is returned
		assertSame(transaction1, EventBus.startTransaction());

		// Manually clear the transaction, as it's a thread local
		EventBus.setCurrentTransaction(null);

		// Change the property to false - which shouldn't matter
		// in this case - and start another transaction
		EventBus.setAutomaticTransactionStart(false);
		EventTransaction transaction2 = EventBus.startTransaction();
		assertNotNull(transaction2);
		assertNotSame(transaction1, transaction2);
	}

	/**
	 * This will test the {@link EventBus#fire(Object)} method, simply from the
	 * perspective of transactions being started or not and how the bus
	 * interacts with transactions.
	 */
	public void testFire_EventBusTransactionInteraction() throws Exception {
		// First, checking the firing of a valid event when
		// the automatic transaction start property is true.
		// Transaction should be started automatically in this
		// case, so the firing should work fine
		EventBus.fire(new SampleEvent());
		assertNotNull(EventBus.getCurrentTransaction());

		try {
			// Firing an invalid event, and an EventException should
			// be thrown
			EventBus.fire(new SampleInvalidEvent());
			fail("EventException should've been thrown");
		} catch (EventException e) {
		}

		// Manually clear the transaction, as it's a thread local
		EventBus.setCurrentTransaction(null);

		// Set the property to false, so the firing should fail
		// as no transaction exists
		EventBus.setAutomaticTransactionStart(false);
		try {
			EventBus.fire(new SampleEvent());
			fail("EventException should've been thrown!");
		} catch (EventException e) {
		}
		// Ensure there's no current transaction
		assertNull(EventBus.getCurrentTransaction());
	}

	public void testRegisterHandlerObject() throws Exception {
		// Register a handler and assert it's been added
		EventBus.register(new SampleClassBasedSingleEventListener());
		List<Handler> handlers = EventBus.getHandlers(new EventImpl(new SampleEvent("1")));
		assertEquals(1, handlers.size());
		// Add another handler, instance of the same class
		// added before. Shouldn't really matter, framework
		// doesn't keep track of this level of duplication
		EventBus.register(new SampleClassBasedSingleEventListener());
		handlers = EventBus.getHandlers(new EventImpl(new SampleEvent("1")));
		assertEquals(2, handlers.size());
	}

	public void testRegisterHandlerClass() throws Exception {
		// Register the class with the handlers and
		// ensure it's been registed
		EventBus.register(SampleClassBasedSingleEventListener.class);
		List<Handler> handlers = EventBus.getHandlers(new EventImpl(new SampleEvent("1")));
		assertEquals(1, handlers.size());
		// Add another handler, instance of the same class
		// added before. Shouldn't really matter, framework
		// doesn't keep track of this level of duplication
		EventBus.register(SampleClassBasedSingleEventListener.class);
		handlers = EventBus.getHandlers(new EventImpl(new SampleEvent("1")));
		assertEquals(2, handlers.size());
	}
}
