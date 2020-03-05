package org.freeatalk.freeone.springboot.activiti6.utils;

public class ResultBack<T> {

	private boolean success = true;

	private int code = 00;

	private int status = 200;

	private String message = "操作成功";

	private T data = null;

	public ResultBack(String message) {
		this.message = message;
	}

	public ResultBack() {
	}

	public ResultBack(T data) {
		this.data = data;
	}

	public ResultBack(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ResultBack(boolean success, String message, T data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public ResultBack(boolean success, int status, T data, String message) {
		super();
		this.success = success;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ResultBack(boolean success, int code, int status, String message, T data) {
		super();
		this.success = success;
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}