package net.janga.javafire.eventing.core;

import net.janga.javafire.eventing.core.EventBus;
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
