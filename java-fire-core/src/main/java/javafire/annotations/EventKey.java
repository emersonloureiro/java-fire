package javafire.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to determine the method in an event class that provides the event
 * key. If more than one method is annotated with this, an exception will be
 * thrown at runtime.
 * 
 * @author Emerson Loureiro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventKey {
}
