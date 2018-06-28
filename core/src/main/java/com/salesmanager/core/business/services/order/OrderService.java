package com.salesmanager.core.business.services.order;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderCriteria;
import com.salesmanager.core.model.order.OrderList;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.order.orderproduct.OrderProduct;
import com.salesmanager.core.model.order.orderstatus.OrderStatusHistory;
import com.salesmanager.core.model.payments.Payment;
import com.salesmanager.core.model.payments.Transaction;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;



public interface OrderService extends SalesManagerEntityService<Long, Order> {

    void addOrderStatusHistory(Order order, OrderStatusHistory history)
                    throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in checkout page
     * @param orderSummary
     * @param customer
     * @param store
     * @param language
     * @return
     * @throws ServiceException
     */
    OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
                                         Customer customer, MerchantStore store, Language language)
                                                         throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in a ShoppingCart
     * @param orderSummary
     * @param store
     * @param language
     * @return
     * @throws ServiceException
     */
    OrderTotalSummary caculateOrderTotal(OrderSummary orderSummary,
                                         MerchantStore store, Language language) throws ServiceException;


    /**
     * Can be used to calculates the final prices of all items contained in checkout page
     * @param shoppingCart
     * @param customer
     * @param store
     * @param language
     * @return  @return {@link OrderTotalSummary}
     * @throws ServiceException
     */
    OrderTotalSummary calculateShoppingCartTotal(final ShoppingCart shoppingCart,final Customer customer, final MerchantStore store, final Language language) throws ServiceException;

    /**
     * Can be used to calculates the final prices of all items contained in a ShoppingCart
     * @param shoppingCart
     * @param store
     * @param language
     * @return {@link OrderTotalSummary}
     * @throws ServiceException
     */
    OrderTotalSummary calculateShoppingCartTotal(final ShoppingCart shoppingCart,final MerchantStore store, final Language language) throws ServiceException;

    ByteArrayOutputStream generateInvoice(MerchantStore store, Order order,
                                          Language language) throws ServiceException;

    Order getOrder(Long id);

    //List<Order> listByStore(MerchantStore merchantStore);

    

    
    /**
     * For finding orders. Mainly used in the administration tool
     * @param store
     * @param criteria
     * @return
     */
    OrderList listByStore(MerchantStore store, OrderCriteria criteria);

    void saveOrUpdate(Order order) throws ServiceException;

	Order processOrder(Order order, Customer customer,
			List<ShoppingCartItem> items, OrderTotalSummary summary,
			MerchantStore store) throws ServiceException;

	Order processOrder(Order order, Customer customer,
			List<ShoppingCartItem> items, OrderTotalSummary summary,
			Transaction transaction, MerchantStore store)
			throws ServiceException;



	
	/**
	 * Determines if an Order has download files
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	boolean hasDownloadFiles(Order order) throws ServiceException;

	List<Order> findOrdersByCustomer(Long id);

	Page<Order> findPaginatedOrdersByCustomer(Long id, Pageable pageable);

	Page<Order> findByDatePurchasedBetween(Date startDate, Date endDate, Pageable pageable);

	Page<Order> findVendorPaginatedOrders(Long id, Pageable pageable);

	List<Order> findOrdersByVendor(Date startDate, Date endDate, Long value);

	List<Order> findOrdersByProduct(Date startDate, Date endDate, String value);

	List<BigInteger> findVendorIds(Date startDate, Date endDate);

	List<String> findProductSkus(Date startDate, Date endDate);

	List<OrderProduct> findOrderProductByVendorIdAndSku(Long vendorId, String productSku);

	List<BigInteger> findRevenueVendors(Date startDate, Date endDate);

	List<BigInteger> searchRevenueVendors();

	List<Order> findOrdersSearchyVendor(long vendorId);

	List<String> searchRevenueProducts();

	List<Order> findOrdersSearchByProduct(String productSku);

	Page<Order> adminSearchOrdersByDatePurchasedBetween(Date fromDate, Date toDate, Long orderId,
			Pageable pageable);

	Page<Order> adminSearchOrdersByDatePurchasedBetweenAndName(Date fromDate, Date toDate, String searchString,
			Pageable pageable);

	List<Order> searchOrdersByCustomerName(Date fromDate, Date toDate, String searchString);
}
