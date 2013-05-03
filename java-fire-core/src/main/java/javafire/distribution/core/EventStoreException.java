package javafire.distribution.core;

/**
 * Class of exception thrown by operations involving the event store.
 * 
 * @author Emerson Loureiro
 */
@SuppressWarnings("serial")
public class EventStoreException extends RuntimeException {

	public EventStoreException(Exception cause) {
		super(cause);
	}
}
