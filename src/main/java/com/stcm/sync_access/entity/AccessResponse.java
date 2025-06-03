package com.stcm.sync_access.entity;

public class AccessResponse {

	private int idCde;
	private int idCC;
	private String transaction;
	private String fileName;
	private int status;
	private int code;
	private String message;
	private String description;
	
	
	public int getIdCde() {
		return idCde;
	}
	public void setIdCde(int idCde) {
		this.idCde = idCde;
	}
	public int getIdCC() {
		return idCC;
	}
	public void setIdCC(int idCC) {
		this.idCC = idCC;
	}
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
