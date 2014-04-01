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
package cf.janga.javafire.tests;

import cf.janga.javafire.annotations.Event;
import cf.janga.javafire.annotations.EventKey;

@Event
public class SampleEvent {

	private final String key;

	public SampleEvent() {
		this(null);
	}

	public SampleEvent(String key) {
		this.key = key;
	}

	@EventKey
	public String getKey() {
		return this.key;
	}
}
