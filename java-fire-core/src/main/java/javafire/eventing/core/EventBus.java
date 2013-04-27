package javafire.eventing.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafire.eventing.utils.EventingUtils;

/**
 * The event bus in <code>Fire</code>. It's a central class where event firing
 * and handler registration takes place.
 * 
 * @author Emerson Loureiro
 */
public final class EventBus {

	/**
	 * The transaction for the current thread.
	 */
	private static ThreadLocal<EventTransaction> currentTransaction = new ThreadLocal<EventTransaction>();

	/**
	 * Whether transactions should be started automatically when needed or not.
	 */
	private static volatile boolean automaticTransactionStart = true;

	/**
	 * Map from event types to its registered handlers.
	 */
	private static Map<Class<?>, List<Handler>> handlersMap = Collections.synchronizedMap(new HashMap<Class<?>, List<Handler>>());

	private EventBus() {
	}

	/**
	 * Starts new event transaction.
	 * 
	 * @return {@link EventTransaction}
	 */
	public static EventTransaction startTransaction() {
		if (currentTransaction.get() == null) {
			currentTransaction.set(new EventTransactionImpl());
		}
		return currentTransaction.get();
	}

	/**
	 * Returns the current thread's transaction. If the automatic transaction
	 * start property is enabled, a transaction is automatically started and
	 * returned, otherwise null is returned. See
	 * {@link EventBus#setAutomaticTransactionStart(boolean)}.
	 * 
	 * @return {@link EventTransaction}
	 */
	public static EventTransaction getCurrentTransaction() {
		if (currentTransaction.get() == null && automaticTransactionStart) {
			currentTransaction.set(new EventTransactionImpl());
		}
		return currentTransaction.get();
	}

	/**
	 * Fires the event represented by the object provided.
	 * 
	 * @param event
	 * @throws EventException
	 *             If the object's class is not a valid event type, no current
	 *             transaction exists, or there is some serious underlying
	 *             problem with the current transaction.
	 */
	public static void fire(Object event) throws EventException {
		// If there's no current transaction, and we're not automatically
		// creating one, throw an exception as we can't process the event
		// without a transaction.
		if ((currentTransaction.get() == null && !automaticTransactionStart) || !EventingUtils.isValidEventType(event.getClass())) {
			throw new EventException("Firing of events is not allowed without an active transaction");
		}
		try {
			// Pass the event to the current transaction
			((InternalEventTransaction) getCurrentTransaction()).eventFired(new EventImpl(event));
		} catch (TransactionException e) {
			throw new EventException(e);
		}
	}

	/**
	 * Sets the value of the "automatic transaction start" property, which
	 * indicates whether an event transaction should be automatically started
	 * when required, and no current one exists.
	 * 
	 * @param value
	 *            The value for the property; true if transactions are to be
	 *            started automatically when needed and false otherwise.
	 */
	public static void setAutomaticTransactionStart(boolean value) {
		automaticTransactionStart = value;
	}

	/**
	 * Registers the provided object as handler against the events it's
	 * interested in.
	 * 
	 * @param handler
	 *            The handler to be registered
	 * @throws EventException
	 *             If the class for the object provided isn't annotated in the
	 *             proper way.
	 */
	public static void register(Object handler) throws EventException {
		if (!EventingUtils.isValidHandler(handler.getClass())) {
			throw new EventException("Class of the object provided isn't annotated as a proper handler. Class: " + handler.getClass());
		}
		List<Method> handlerMethods = EventingUtils.getHandlerMethods(handler.getClass());
		for (Method handlerMethod : handlerMethods) {
			register(new HandlerImpl(handler, handlerMethod));
		}
	}

	public static void register(Class<?> handlerClass) throws EventException {
		if (!EventingUtils.isValidHandler(handlerClass)) {
			throw new EventException("Class provided isn't annotated as a proper handler. Class: " + handlerClass);
		}
		List<Method> handlerMethods = EventingUtils.getHandlerMethods(handlerClass);
		for (Method handlerMethod : handlerMethods) {
			register(new HandlerImpl(handlerClass, handlerMethod));
		}
	}

	static List<Handler> getHandlers(Event event) {
		List<Handler> handlers = handlersMap.get(event.getWrappedEvent().getClass());
		if (handlers != null) {
			return handlers;
		}
		return Collections.emptyList();
	}

	static void transactionCommitted(EventTransaction transaction) {
		currentTransaction.set(null);
	}

	static void transactionRolledback(EventTransaction transaction) {
		currentTransaction.set(null);
	}

	/**
	 * Sets the current thread's transaction to the one provided. This is made
	 * package-private as its meant to simply support unit testing.
	 */
	static void setCurrentTransaction(EventTransaction transaction) {
		currentTransaction.set(transaction);
	}

	private static void register(Handler handlerWrapper) {
		Class<?> eventClass = handlerWrapper.getEventClass();
		List<Handler> handlersForEvent = handlersMap.get(eventClass);
		if (handlersForEvent == null) {
			handlersForEvent = Collections.synchronizedList(new ArrayList<Handler>());
			handlersMap.put(eventClass, handlersForEvent);
		}
		handlersForEvent.add(handlerWrapper);
	}

	public static void clearHandlers() {
		handlersMap.clear();
	}
}
