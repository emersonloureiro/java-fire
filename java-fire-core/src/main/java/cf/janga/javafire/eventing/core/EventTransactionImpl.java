package cf.janga.javafire.eventing.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Default implementation of an event transaction.
 * 
 * @author Emerson Loureiro
 */
class EventTransactionImpl implements InternalEventTransaction {

	/** Keeps the unique identifier of all events fired within this transaction. */
	private final Set<String> firedEventIds;

	/** Identifier of this transaction. */
	private final String id;

	/** Current status of the transaction. */
	private volatile TransactionStatus status;

	private enum TransactionStatus {
		STARTED, COMMITTED, ROLLED_BACK;
	}

	/**
	 * The queues where synchronous and asynchronous events are kept until the
	 * transaction is committed and the events then processed.
	 */
	private final EventQueue synchronousEventQueue;

	public EventTransactionImpl() {
		this.id = UUID.randomUUID().toString();
		this.firedEventIds = new LinkedHashSet<String>();
		this.synchronousEventQueue = new EventQueueImpl();
		this.status = TransactionStatus.STARTED;
	}

	@Override
	public void commit() throws EventException, TransactionException {
		if (!isActive()) {
			throw new TransactionException("Transaction is not active. Transaction id: " + this.id, this.id);
		}
		try {
			processSynchronousEvents();
			this.status = TransactionStatus.COMMITTED;
			EventBus.transactionCommitted(this);
		} catch (EventException e) {
			rollback();
			throw e;
		} catch (Exception e) {
			rollback();
			throw new TransactionException(e, this.id);
		}
	}

	@Override
	public void rollback() throws TransactionException {
		this.synchronousEventQueue.clear();
		this.status = TransactionStatus.ROLLED_BACK;
		EventBus.transactionRolledback(this);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void eventFired(Event event) throws TransactionException {
		if (!isActive()) {
			throw new TransactionException("Transaction is not active. Transaction id: " + this.id, this.id);
		}
		if (!this.firedEventIds.contains(event.getId()) || event.isLastEventWins()) {
			this.firedEventIds.add(event.getId());
			if (event.isSynchronous()) {
				this.synchronousEventQueue.add(event);
			}
		}
	}

	@Override
	public boolean isRolledBack() {
		return this.status == TransactionStatus.ROLLED_BACK;
	}

	@Override
	public boolean isCommitted() {
		return this.status == TransactionStatus.COMMITTED;
	}

	Set<String> getFiredEventIds() {
		return this.firedEventIds;
	}

	EventQueue getSynchronousEventQueue() {
		return this.synchronousEventQueue;
	}

	private boolean isActive() {
		return this.status != TransactionStatus.COMMITTED && this.status != TransactionStatus.ROLLED_BACK;
	}

	private void processSynchronousEvents() throws EventException {
		while (!this.synchronousEventQueue.isEmpty()) {
			Event event = this.synchronousEventQueue.take();
			List<Handler> handlers = EventBus.getHandlers(event);
			for (Handler handler : handlers) {
				handler.handle(event);
			}
		}
	}
}
