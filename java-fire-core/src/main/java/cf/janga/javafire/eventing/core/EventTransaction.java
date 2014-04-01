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

/**
 * A transaction where events are fired, and later processed.
 * 
 * @author Emerson Loureiro
 */
public interface EventTransaction {

	/**
	 * Commits this transaction, processing all events fired within it.
	 * Synchronous events will be processed sequentially on the order they've
	 * been fired (i.e., FIFO). Any {@link Exception} thrown while processing
	 * any of the events will automatically cause this transaction to rollback,
	 * and a {@link TransactionException} to be thrown. See
	 * {@link EventTransaction#rollback()}.
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
	 */
	void rollback();

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
