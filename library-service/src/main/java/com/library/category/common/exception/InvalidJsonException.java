package com.library.category.common.exception;

public class InvalidJsonException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidJsonException(final Throwable e) {
		super(e);
	}

	public InvalidJsonException(final String string) {
		super(string);
	}

}
