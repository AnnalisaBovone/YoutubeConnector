package com.example.youtubeException;

public class YoutubeException extends Exception {
	private ErrorCode errorCode;
	
	public YoutubeException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
		System.out.println(message);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
