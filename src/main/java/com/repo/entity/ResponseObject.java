package com.repo.entity;

public class ResponseObject {
	
	private Boolean status;
	
	private int code;
	
	private String message;

	/**
	 * @param status
	 * @param code
	 * @param message
	 */
	public ResponseObject(Boolean status, int code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}

	/**
	 * 
	 */
	public ResponseObject() {
		super();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
