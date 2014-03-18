package net.janga.javafire.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes the annotated class as an event type. An event type exists -
 * currently only supporting {@link EventType#SYNCHRONOUS} - which is the
 * default so it doesn't need to be provided. Duplication resolution can be
 * determined with {@link DuplicateResolution}.
 * 
 * @author Emerson Loureiro
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {

	public enum EventType {
		SYNCHRONOUS
	}

	public enum DuplicateResolution {
		FIRST_WINS, LAST_WINS
	}

	EventType type() default EventType.SYNCHRONOUS;

	DuplicateResolution duplicateResolution() default DuplicateResolution.FIRST_WINS;
}
