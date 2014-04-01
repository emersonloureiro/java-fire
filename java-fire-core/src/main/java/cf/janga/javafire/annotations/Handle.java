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
package cf.janga.javafire.annotations;

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
