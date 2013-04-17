package javafire.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is part of the event properties. This is
 * only relevant for asynchronous events, and is therefore ignored if provided
 * for synchronous ones.
 * 
 * @author Emerson Loureiro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventProperty {
}
