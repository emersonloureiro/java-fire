package javafire.eventing.core;

import java.util.Map;

import javafire.annotations.EventProperty;

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
	 * Whether this event is synchronous or not.
	 * 
	 * @return True if synchronous and false otherwise.
	 */
	boolean isSynchronous();

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

	/**
	 * Returns a map of all the properties of the event wrapped by this type.
	 * More precisely, returns the values of all methods, on the wrapped event,
	 * which are annotated with the {@link EventProperty} annotation. For an
	 * event with no property, an empty map is returned instead.
	 * 
	 * @return {@link Map}
	 */
	Map<String, Object> getProperties();
}
