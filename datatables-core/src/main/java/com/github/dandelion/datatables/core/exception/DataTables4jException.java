package com.github.dandelion.datatables.core.exception;

public class DataTables4jException extends RuntimeException {

	private static final long serialVersionUID = 5701314549599481318L;

	public DataTables4jException() {
	};

	public DataTables4jException(String message) {
		super(message);
	}

	public DataTables4jException(Throwable cause) {
		super(cause);
	}

	public DataTables4jException(String message, Throwable cause) {
		super(message, cause);
	}
}
