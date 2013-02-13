package com.github.dandelion.datatables.core.exception;

public class DataTableProcessingException extends RuntimeException {

	private static final long serialVersionUID = 5701314549599481318L;

	public DataTableProcessingException() {
	};

	public DataTableProcessingException(String message) {
		super(message);
	}

	public DataTableProcessingException(Throwable cause) {
		super(cause);
	}

	public DataTableProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
