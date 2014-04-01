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
package cf.janga.javafire.tests.endtoend.general;

import cf.janga.javafire.annotations.Handle;

public class HandlerFiredEventListener {

	public static int handlerFiredSynchronousEventsProcessed = 0;

	@Handle
	public void handleListenerFiredEvent(HandlerFiredSynchronousEvent event) {
		handlerFiredSynchronousEventsProcessed++;
	}
}
