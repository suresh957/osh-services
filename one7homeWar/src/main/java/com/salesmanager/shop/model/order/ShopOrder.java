package com.salesmanager.shop.model.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.shipping.ShippingOption;
import com.salesmanager.core.model.shipping.ShippingSummary;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.model.customer.Address;


/**
 * Orders saved on the website
 * @author Carl Samson
 *
 */
public class ShopOrder extends PersistableOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ShoppingCartItem> shoppingCartItems;//overrides parent API list of shoppingcartitem

	private OrderTotalSummary orderTotalSummary;//The order total displayed to the end user. That object will be used when committing the order
	
	
	private ShippingSummary shippingSummary;
	private ShippingOption selectedShippingOption = null;//Default selected option
	
	private String defaultPaymentMethodCode = null;
	
	private Long shippingCharges;
	private String paymentMethodType = null;//user selected payment type
	private Map<String,String> payment;//user payment information
	
	private String errorMessage = null;
	//private Address deliveryAddress = null;
	private Integer preferedShippingAddress;
	private String ipAddress;

	public Long getShippingCharges() {
		return shippingCharges;
	}
	public void setShippingCharges(Long shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
		this.shoppingCartItems = shoppingCartItems;
	}
	public List<ShoppingCartItem> getShoppingCartItems() {
		return shoppingCartItems;
	}

	public void setOrderTotalSummary(OrderTotalSummary orderTotalSummary) {
		this.orderTotalSummary = orderTotalSummary;
	}
	public OrderTotalSummary getOrderTotalSummary() {
		return orderTotalSummary;
	}

	public ShippingSummary getShippingSummary() {
		return shippingSummary;
	}
	public void setShippingSummary(ShippingSummary shippingSummary) {
		this.shippingSummary = shippingSummary;
	}
	public ShippingOption getSelectedShippingOption() {
		return selectedShippingOption;
	}
	public void setSelectedShippingOption(ShippingOption selectedShippingOption) {
		this.selectedShippingOption = selectedShippingOption;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getPaymentMethodType() {
		return paymentMethodType;
	}
	public void setPaymentMethodType(String paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}
	public Map<String,String> getPayment() {
		return payment;
	}
	public void setPayment(Map<String,String> payment) {
		this.payment = payment;
	}
	public String getDefaultPaymentMethodCode() {
		return defaultPaymentMethodCode;
	}
	public void setDefaultPaymentMethodCode(String defaultPaymentMethodCode) {
		this.defaultPaymentMethodCode = defaultPaymentMethodCode;
	}
	/*public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}*/
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Integer getPreferedShippingAddress() {
		return preferedShippingAddress;
	}
	public void setPreferedShippingAddress(Integer preferedShippingAddress) {
		this.preferedShippingAddress = preferedShippingAddress;
	}



}
