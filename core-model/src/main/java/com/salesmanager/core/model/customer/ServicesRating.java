package com.salesmanager.core.model.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.services.Services;

@Entity
@Table(name = "SERVICES_RATING", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ServicesRating extends SalesManagerEntity<Long, ServicesRating> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SERVICES_RATING_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
 
	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@Column(name="RATING")
	private Integer rating;

	@Column(name="REVIEW_DESC", length=500)
    private String reviewDescription;
	
	@Column(name="REVIEW_TITLE")
    private String reviewTitle;
    
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "SERVICE_ID", nullable = false)
    private Customer service;

	@ManyToOne(targetEntity = Services.class)
	@JoinColumn(name = "SERVICE_TYPE_ID", nullable = false)
    private Services serviceType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewDescription() {
		return reviewDescription;
	}

	public void setReviewDescription(String reviewDescription) {
		this.reviewDescription = reviewDescription;
	}

	public String getReviewTitle() {
		return reviewTitle;
	}

	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getService() {
		return service;
	}

	public void setService(Customer service) {
		this.service = service;
	}

	public Services getServiceType() {
		return serviceType;
	}

	public void setServiceType(Services serviceType) {
		this.serviceType = serviceType;
	}

}
