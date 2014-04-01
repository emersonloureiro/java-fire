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
 * Representation of an event, which wraps the actual event object, providing
 * easy access methods for its properties. The contract of this interface is
 * that no validation or check is performed, i.e., whichever event is wrapped
 * within an implementation of this type, it's expected to be a valid event
 * type.
 * 
 * @author Emerson Loureiro
 */
public interface Event {

	/**
	 * Returns the key of this event.
	 * 
	 * @return String
	 */
	String getKey();

	/**
	 * Returns the unique identifier of this event.
	 * 
	 * @return String
	 */
	String getId();

	/**
	 * Returns whether, for the type of this event, the last event is the one
	 * that matters, when two events having the same identifier are fired within
	 * the same transaction.
	 * 
	 * @return True if the last event is the one that matters and false
	 *         otherwise.
	 */
	boolean isLastEventWins();

	/**
	 * Returns the object representing the actual event.
	 * 
	 * @return Object
	 */
	Object getWrappedEvent();
}
