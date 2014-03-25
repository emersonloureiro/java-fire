package cf.janga.javafire.eventing.core;

import java.lang.reflect.Method;

import org.apache.commons.lang.UnhandledException;

import cf.janga.javafire.annotations.Event.DuplicateResolution;
import cf.janga.javafire.annotations.Event.EventType;
import cf.janga.javafire.eventing.utils.EventingUtils;

/**
 * Default implementation of {@link Event}.
 * 
 * @author Emerson Loureiro
 */
class EventImpl implements Event {

	private final Object event;

	private final Method eventKeyMethod;

	public EventImpl(Object event) {
		this.event = event;
		this.eventKeyMethod = EventingUtils.getEventKeyMethods(this.event.getClass()).get(0);
	}

	@Override
	public String getKey() {
		try {
			return (String) this.eventKeyMethod.invoke(this.event);
		} catch (Exception e) {
			throw new UnhandledException("Error getting key for event " + this.event.getClass(), e);
		}
	}

	@Override
	public boolean isSynchronous() {
		return EventingUtils.getEventAnnotation(this.event.getClass()).type() == EventType.SYNCHRONOUS;
	}

	@Override
	public boolean isLastEventWins() {
		return EventingUtils.getEventAnnotation(this.event.getClass()).duplicateResolution() == DuplicateResolution.LAST_WINS;
	}

	@Override
	public Object getWrappedEvent() {
		return this.event;
	}

	@Override
	public String getId() {
		return getKey() + "-" + this.event.getClass().getName();
	}
}
