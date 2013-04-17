package javafire.eventing.core;

/**
 * Class of exceptions related to events (e.g., firing events and registering
 * handlers).
 * 
 * @author Emerson Loureiro
 */
public class EventException extends Exception {

	public EventException(Exception cause) {
		super(cause);
	}

	public EventException(String message) {
		super(message);
	}
}
