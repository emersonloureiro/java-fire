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
 * A representation for types that can handle events.
 * 
 * @author Emerson Loureiro
 */
interface Handler {

	/**
	 * Passes the given event to be handled by this handler.
	 * 
	 * @param event
	 *            The event to be handled.
	 * @throws EventException
	 *             If there's any exception while processing the event.
	 */
	void handle(Event event) throws EventException;

	/**
	 * Returns the class of the event this handler is handling
	 * 
	 * @return Class
	 */
	Class<?> getEventClass();
}
