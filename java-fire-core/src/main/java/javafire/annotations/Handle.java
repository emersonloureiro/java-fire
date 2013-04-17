package javafire.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the annotated method handles events of the type defined by the
 * method argument. The annotated method should have a <code>void</code> return
 * type and a single parameter, whose class is annotated with {@link Event}.
 * 
 * @author Emerson Loureiro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handle {
}
