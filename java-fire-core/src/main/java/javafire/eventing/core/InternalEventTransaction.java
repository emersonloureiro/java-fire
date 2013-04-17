package javafire.eventing.core;

/**
 * This interface extends the exposed {@link EventTransaction} one to provide
 * functionality needed by the framework that doesn't require/shouldn't be
 * exposed as API. Consequently, it's defined with package-level access only.
 * Internally, any event transaction implementation should derive from this
 * interface.
 * 
 * @author Emerson Loureiro
 */
interface InternalEventTransaction extends EventTransaction {

	/**
	 * Notifies this transaction that an event has been fired within it, through
	 * the event bus. The transaction expects the object provided to represent a
	 * valid event (i.e., contains the proper annotations), so any validation is
	 * to be done prior to calling this method.
	 * 
	 * @param event
	 *            The event that's been fired.
	 * @throws TransactionException
	 *             If there's any error processing when caching the event for
	 *             later processing.
	 */
	void eventFired(Event event) throws TransactionException;
}
