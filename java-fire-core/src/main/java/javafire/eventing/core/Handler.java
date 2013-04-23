package javafire.eventing.core;

import javafire.api.Event;

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
