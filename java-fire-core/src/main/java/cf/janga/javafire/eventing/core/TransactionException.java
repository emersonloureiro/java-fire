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

/**
 * Class of exception thrown by an {@link EventTransaction}.
 * 
 * @author Emerson Loureiro
 */
@SuppressWarnings("serial")
public class TransactionException extends Exception {

	private final String transactionId;

	public TransactionException(Exception cause, String transactionId) {
		super(cause);
		this.transactionId = transactionId;
	}

	public TransactionException(String message, String transactionId) {
		super(message);
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return this.transactionId;
	}
}
