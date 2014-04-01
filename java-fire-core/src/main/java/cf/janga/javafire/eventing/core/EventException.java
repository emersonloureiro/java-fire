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

/**
 * Class of exceptions related to events (e.g., firing events and registering
 * handlers).
 * 
 * @author Emerson Loureiro
 */
@SuppressWarnings("serial")
public class EventException extends RuntimeException {

	public EventException(Exception cause) {
		super(cause);
	}

	public EventException(String message) {
		super(message);
	}
}
