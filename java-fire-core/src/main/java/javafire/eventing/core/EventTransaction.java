package javafire.eventing.core;

/**
 * A transaction where events are fired, and later processed.
 * 
 * @author Emerson Loureiro
 */
public interface EventTransaction {

	/**
	 * Commits this transaction, processing all events fired within it.
	 * Synchronous events will be processed sequentially on the order they've
	 * been fired (i.e., FIFO), whereas asynchronous ones will be serialized and
	 * dispatched to be processed on separate transactions, possibly remotely.
	 * Any {@link Exception} thrown while processing any of the synchronous
	 * events, or dispatching any of the asynchronous ones, will automatically
	 * cause this transaction to rollback, and a {@link TransactionException} to
	 * be thrown. See {@link EventTransaction#rollback()}.
	 * 
	 * @throws EventException
	 *             If there's any error processing/dispatching the events for
	 *             this transaction, including errors within the handlers for
	 *             each event.
	 * @throws TransactionException
	 *             If there's any other error when committing the transaction
	 */
	void commit() throws EventException, TransactionException;

	/**
	 * Rolls back this transaction, clearing up all events that have been fired
	 * within it.
	 * 
	 * @throws TransactionException
	 */
	void rollback() throws TransactionException;

	/**
	 * Checks whether this transaction has been rolled back or not.
	 * 
	 * @return True if the transaction has been rolled back and false otherwise.
	 */
	boolean isRolledBack();

	/**
	 * Checks whether this transaction has been committed or not.
	 * 
	 * @return True if the transaction has been committed and false otherwise.
	 */
	boolean isCommitted();

	/**
	 * Returns the identifier of this transaction.
	 * 
	 * @return String
	 */
	String getId();
}
