package com.salesmanager.shop.controller.vendor;

public class AdminMachineryPortfolioResponse {

	private String successMessage;
	private String errorMessgae;
	private boolean status;
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorMessgae() {
		return errorMessgae;
	}
	public void setErrorMessgae(String errorMessgae) {
		this.errorMessgae = errorMessgae;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
