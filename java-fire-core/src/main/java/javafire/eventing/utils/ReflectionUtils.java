package javafire.eventing.utils;

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
