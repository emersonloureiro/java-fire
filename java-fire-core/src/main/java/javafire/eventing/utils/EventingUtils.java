package javafire.eventing.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafire.annotations.Event;
import javafire.annotations.EventKey;
import javafire.annotations.Handle;

public class EventingUtils {

	private EventingUtils() {
	}

	public static List<Method> getHandlerMethods(Class<?> handlerClass) {
		Method[] declaredMethods = handlerClass.getDeclaredMethods();
		List<Method> handlerMethods = new ArrayList<Method>();
		for (Method method : declaredMethods) {
			if (ReflectionUtils.hasAnnotation(method, Handle.class) && ReflectionUtils.hasThisManyParameters(method, 1)
					&& isValidEventType(ReflectionUtils.getParameterType(method, 0)) && ReflectionUtils.hasReturnType(method, void.class)) {
				handlerMethods.add(method);
			}
		}
		return handlerMethods;
	}

	public static boolean isValidHandler(Class<?> handlerClass) {
		Method[] declaredMethods = handlerClass.getDeclaredMethods();
		int handleMethodsFound = 0;
		for (Method method : declaredMethods) {
			if (ReflectionUtils.hasAnnotation(method, Handle.class)) {
				if (ReflectionUtils.hasThisManyParameters(method, 1) && isValidEventType(ReflectionUtils.getParameterType(method, 0))
						&& ReflectionUtils.hasReturnType(method, void.class)) {
					handleMethodsFound++;
				} else {
					return false;
				}
			}
		}
		return handleMethodsFound > 0;
	}

	public static boolean isValidEventType(Class<?> objectClass) {
		if (objectClass == null) {
			return false;
		}
		// A valid event type has the Event annotation
		// and only one method annotated with EventKey
		return (getEventAnnotation(objectClass) != null) && (getEventKeyMethods(objectClass).size() == 1);
	}

	public static Event getEventAnnotation(Class<?> objectClass) {
		if (objectClass == null) {
			return null;
		}
		return objectClass.getAnnotation(Event.class);
	}

	public static List<Method> getEventKeyMethods(Class<?> objectClass) {
		if (objectClass == null) {
			return new ArrayList<Method>(0);
		}
		Method[] declaredMethods = objectClass.getDeclaredMethods();
		List<Method> eventKeyMethods = new ArrayList<Method>();
		for (Method declaredMethod : declaredMethods) {
			if (ReflectionUtils.isPublic(declaredMethod) && ReflectionUtils.hasReturnType(declaredMethod, String.class) && ReflectionUtils.hasNoParameter(declaredMethod)
					&& ReflectionUtils.hasAnnotation(declaredMethod, EventKey.class)) {
				eventKeyMethods.add(declaredMethod);
			}
		}
		return eventKeyMethods;
	}
}
