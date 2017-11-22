package com.msn.poc.user.exception;

public class TokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TokenException(){
		super();
	}
	
	public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

}
