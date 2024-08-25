package com.techeazy.studentmanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceNotFoundException extends Exception {
	
	private boolean errorCode;
	private String msg;
	
	
	public boolean isError() {
		return errorCode;
	}
	public void setError(boolean error) {
		this.errorCode = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}