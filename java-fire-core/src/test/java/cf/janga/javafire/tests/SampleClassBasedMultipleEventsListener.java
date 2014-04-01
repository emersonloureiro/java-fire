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

import cf.janga.javafire.annotations.Handle;

public class SampleClassBasedMultipleEventsListener {

	public static int sampleEventsHandled = 0;
	public static int sampleSynchronousEventsHandled = 0;

	@Handle
	public void handleSomeEvent(SampleEvent event) {
		sampleEventsHandled++;
	}

	@Handle
	public void handleSomeEvent(SampleSynchronousEvent event) {
		sampleSynchronousEventsHandled++;
	}
}
