package com.salesmanager.shop.admin.controller;

public class CustomerTestimonialVO {
    
	private Long customerId;
	private String customerName;
	private String description;
	private String emailAddress;
	private Long testimonialId;
	private String status;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Long getTestimonialId() {
		return testimonialId;
	}
	public void setTestimonialId(Long testimonialId) {
		this.testimonialId = testimonialId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
