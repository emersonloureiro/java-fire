package javafire.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes the annotated class as an event type. A parameter can be provided to
 * mark the event type as synchronous {@link EventType#SYNCHRONOUS} or
 * asynchronous {@link EventType#ASYNCHRONOUS}, which is the default so it
 * doesn't need to be provided.
 * 
 * @author Emerson Loureiro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {

	public enum EventType {
		ASYNCHRONOUS, SYNCHRONOUS
	}

	;

	public enum DuplicateResolution {
		FIRST_WINS, LAST_WINS
	}

	;

	EventType type() default EventType.ASYNCHRONOUS;

	DuplicateResolution duplicateResolution() default DuplicateResolution.FIRST_WINS;
}
