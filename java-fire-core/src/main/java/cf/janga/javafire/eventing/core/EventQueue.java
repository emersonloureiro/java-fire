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
 * A queue where events are kept until ready to be processed. It guarantees no
 * duplicate events can ever exist in it. More precisely, if an event
 * <code>e1</code> exists in the queue, and another event <code>e2</code> is
 * added, such that <code>e1.getId().equals(e2.getId())</code>, then
 * <code>e2</code> will replace <code>e1</code> in the queue, in the very same
 * position <code>e1</code> was placed.
 * 
 * @author Emerson Loureiro
 */
interface EventQueue {

	/**
	 * Returns the event at the front of the queue, without removing it.
	 * 
	 * @return {@link Event}
	 */
	Event peek();

	/**
	 * Removes and returns the event at the front of the queue.
	 * 
	 * @return {@link Event}
	 */
	Event take();

	/**
	 * Adds the given event to the queue.
	 * 
	 * @param event
	 *            The event to be added
	 */
	void add(Event event);

	/**
	 * Removes all events in this queue.
	 */
	void clear();

	/**
	 * Returns the current size of the queue.
	 * 
	 * @return int
	 */
	int size();

	/**
	 * Checks whether this queue is empty.
	 * 
	 * @return True if the queue is empty and false otherwise.
	 */
	boolean isEmpty();
}
