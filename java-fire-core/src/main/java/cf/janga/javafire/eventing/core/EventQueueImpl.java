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

import java.util.LinkedHashMap;
import java.util.Map;

class EventQueueImpl implements EventQueue {

	private final Map<String, Event> queue;

	public EventQueueImpl() {
		this.queue = new LinkedHashMap<String, Event>();
	}

	@Override
	public Event peek() {
		return this.queue.values().iterator().next();
	}

	@Override
	public Event take() {
		Event event = this.queue.values().iterator().next();
		this.queue.remove(event.getId());
		return event;
	}

	@Override
	public void add(Event event) {
		this.queue.put(event.getId(), event);
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

	@Override
	public int size() {
		return this.queue.size();
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
}
