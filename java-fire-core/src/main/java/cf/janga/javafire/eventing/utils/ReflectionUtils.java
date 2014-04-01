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
package cf.janga.javafire.eventing.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static boolean isPublic(Method method) {
		return Modifier.isPublic(method.getModifiers());
	}

	public static Class<?> getParameterType(Method method, int parameterIndex) {
		return (method.getParameterTypes())[parameterIndex];
	}

	public static boolean hasReturnType(Method method, Class<?> returnType) {
		return method.getReturnType().equals(returnType);
	}

	public static boolean hasThisManyParameters(Method method, int numberOfParameters) {
		return method.getParameterTypes().length == numberOfParameters;
	}

	public static boolean hasNoParameter(Method method) {
		return hasThisManyParameters(method, 0);
	}

	public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotation) {
		return method.isAnnotationPresent(annotation);
	}
}
