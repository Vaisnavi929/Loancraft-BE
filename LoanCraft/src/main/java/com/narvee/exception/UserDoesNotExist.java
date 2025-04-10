package com.narvee.exception;
public class UserDoesNotExist extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserDoesNotExist(String message) {
        super(message);
    }
}
