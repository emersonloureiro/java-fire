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
package cf.janga.javafire.eventing.core;

import java.lang.reflect.Method;

import cf.janga.javafire.eventing.utils.ReflectionUtils;

/**
 * Default implementation of {@link Handler}. Made package-private as it's only
 * meant to be used internally by the framework.
 * 
 * @author Emerson Loureiro
 */
class HandlerImpl implements Handler {

	private Object handlerObject;

	private Class<?> handlerClass;

	private final Method handlerMethod;

	private HandlerImpl(Method handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public HandlerImpl(Object handlerObject, Method handlerMethod) {
		this(handlerMethod);
		this.handlerObject = handlerObject;
	}

	public HandlerImpl(Class<?> objectClass, Method handlerMethod) {
		this(handlerMethod);
		this.handlerClass = objectClass;
	}

	@Override
	public void handle(Event event) throws EventException {
		try {
			// If the handler is an instance, call the method
			// directly on it
			if (this.handlerObject != null) {
				this.handlerMethod.invoke(this.handlerObject, event.getWrappedEvent());
			} else {
				// If not an instance, it's assumed the handler
				// is a class, so create a new instance and call
				// the method on it.
				this.handlerMethod.invoke(this.handlerClass.newInstance(), event.getWrappedEvent());
			}
		} catch (Exception e) {
			throw new EventException(e);
		}
	}

	@Override
	public Class<?> getEventClass() {
		return ReflectionUtils.getParameterType(this.handlerMethod, 0);
	}
}
