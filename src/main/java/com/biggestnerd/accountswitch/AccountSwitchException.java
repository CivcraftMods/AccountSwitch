package com.biggestnerd.accountswitch;

public class AccountSwitchException extends Exception {

	private final String errorMessage;
	private final ErrorType type;
	
	public AccountSwitchException(ErrorType type, String errorMessage) {
		this.type = type;
		this.errorMessage = errorMessage;
	}

	public String getMessage() {
		return errorMessage;
	}
	
	public ErrorType getErrorType() {
		return type;
	}
	
	public enum ErrorType {
		ENCRYPT, AUTH
	}
	
}
