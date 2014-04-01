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

import cf.janga.javafire.eventing.core.EventBus;
import junit.framework.TestCase;

public abstract class BaseEventingTest extends TestCase {

	@Override
	public final void setUp() {
		setUpImpl();
	}

	@Override
	public final void tearDown() {
		tearDownImpl();
		EventBus.setAutomaticTransactionStart(true);
		EventBus.setCurrentTransaction(null);
		EventBus.clearHandlers();
	}

	public void setUpImpl() {
	}

	public void tearDownImpl() {
	}
}
