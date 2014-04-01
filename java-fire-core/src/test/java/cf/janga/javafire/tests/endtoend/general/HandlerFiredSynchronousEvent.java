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

import cf.janga.javafire.annotations.Event;
import cf.janga.javafire.annotations.EventKey;
import cf.janga.javafire.tests.SampleEvent;

@Event
public class HandlerFiredSynchronousEvent extends SampleEvent {

	public HandlerFiredSynchronousEvent(String id) {
		super(id);
	}

	@Override
	@EventKey
	public String getKey() {
		return super.getKey();
	}
}
