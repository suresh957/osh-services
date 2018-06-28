package com.salesmanager.core.business.services.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.services.ServicesWorkerVO;
import com.salesmanager.core.business.modules.services.WorkerServiceResponse;
import com.salesmanager.core.business.repositories.customer.CustomerRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.customer.attribute.CustomerAttributeService;
import com.salesmanager.core.business.services.services.ServicesRatingService;
import com.salesmanager.core.business.vendor.VendorRatingService;
import com.salesmanager.core.model.common.Address;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.customer.ServicesRating;
import com.salesmanager.core.model.customer.VendorRating;
import com.salesmanager.core.model.customer.attribute.CustomerAttribute;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.modules.utils.GeoLocation;




@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<Long, Customer> implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private CustomerRepository customerRepository;
	
	@Inject
	private CustomerAttributeService customerAttributeService;
	
	@Inject
	private ServicesRatingService servicesRatingService;

	@Inject
	private GeoLocation geoLocation;
	
	@Inject
    private VendorRatingService vendorRatingService;
	
	@Inject
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		super(customerRepository);
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Customer> getByName(String firstName) {
		return customerRepository.findByName(firstName);
	}
	
	@Override
	public Customer getById(Long id) {
			return customerRepository.findOne(id);		
	}
	
	@Override
	public Customer getByNick(String nick) {
		return customerRepository.findByNick(nick);	
	}
	
	@Override
	public Customer getByNick(String nick, int storeId) {
		return customerRepository.findByNick(nick, storeId);	
	}
	
	@Override
	public List<Customer> listByStore(MerchantStore store) {
		return customerRepository.findByStore(store.getId());
	}
	
	@Override
	public CustomerList listByStore(MerchantStore store, CustomerCriteria criteria) {
		LOGGER.debug("Getting customer list by store");
		return customerRepository.listByStore(store,criteria);
	}
	
	@Override
	public Address getCustomerAddress(MerchantStore store, String ipAddress) throws ServiceException {
		
		try {
			return geoLocation.getAddress(ipAddress);
		} catch(Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override	
	public void saveOrUpdate(Customer customer) throws ServiceException {

		LOGGER.debug("Creating Customer");
		
		if(customer.getId()!=null && customer.getId()>0) {
			super.update(customer);
		} else {			
		
			super.create(customer);

		}
	}

	public void delete(Customer customer) throws ServiceException {
		customer = getById(customer.getId());
		LOGGER.debug("Deleting customer");
		//delete attributes
		List<CustomerAttribute> attributes =customerAttributeService.getByCustomer(customer.getMerchantStore(), customer);
		if(attributes!=null) {
			for(CustomerAttribute attribute : attributes) {
				customerAttributeService.delete(attribute);
			}
		}
		customerRepository.delete(customer);

	}

	@Override
	public List<Customer> findByServiceId(Integer serviceId) {
		LOGGER.debug("Invoking findByServiceId repository");
		return customerRepository.findByServiceId(serviceId);
	}
	
	@Override
	public List<ServicesWorkerVO> findByServiceType(String serviceType) {
		LOGGER.debug("Entered findByServiceType");
		WorkerServiceResponse response = new WorkerServiceResponse();
		List<Customer> customer = customerRepository.findByServiceType(serviceType);
		List<ServicesWorkerVO> servicesWorkerVOSet= new ArrayList<ServicesWorkerVO>();
		for(Customer eachWorker : customer){
			Double avgRating = new Double(0);
			int totalRating= 0;
			int totalReviews = 0;
			double totalRate = 0;
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(new Integer(String.valueOf((eachWorker.getId()))));
			servicesWorkerVO.setCompanyName(eachWorker.getVendorAttrs().getVendorName());
			servicesWorkerVO.setImageUrl(eachWorker.getUserProfile());
			/*servicesWorkerVO.setHouseNumber(eachWorker.getVendorAttrs().getVendorOfficeAddress());
			servicesWorkerVO.setStreet(eachWorker.getBilling().getAddress());
			servicesWorkerVO.setArea(eachWorker.getArea());
			servicesWorkerVO.setCity(eachWorker.getBilling().getCity());
			servicesWorkerVO.setState(eachWorker.getBilling().getState());
			servicesWorkerVO.setPinCode(eachWorker.getBilling().getPostalCode());
			servicesWorkerVO.setContactNumber(eachWorker.getBilling().getTelephone());
			servicesWorkerVO.setImageUrl(eachWorker.getVendorAttrs().getVendorAuthCert());
			servicesWorkerVO.setCountry(eachWorker.getBilling().getCountry().getName());*/
			//fetching ratings from services rating
			List<ServicesRating> servicesRatingList = servicesRatingService.getServicesReviews(eachWorker.getId());
			if(servicesRatingList != null) {
				totalReviews = servicesRatingList.size();
				for(ServicesRating servicesRating:servicesRatingList){
					totalRating= totalRating + servicesRating.getRating();
				}
				totalRate = totalRating;
				avgRating = Double.valueOf(totalRate / totalReviews);
				avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			}
			servicesWorkerVO.setAvgRating(avgRating);
			//servicesWorkerVO.setTotalRating(totalRating);
			servicesWorkerVO.setTotalRating(totalReviews);
			servicesWorkerVOSet.add(servicesWorkerVO);
		}
		LOGGER.debug("Ended findByVendorType");
		return servicesWorkerVOSet;
	}
	
	@Override
	public List<ServicesWorkerVO> findByVendorType(String userType) {
		LOGGER.debug("Entered findByVendorType");
		WorkerServiceResponse response = new WorkerServiceResponse();
		List<Customer> customer = customerRepository.findByVendorType(userType);
		List<ServicesWorkerVO> servicesWorkerVOList= new ArrayList<ServicesWorkerVO>();
		for(Customer eachWorker : customer){
			//int avgRating = 0;
			Double avgRating = new Double(0);
			int totalRating= 0;
			int totalReviews = 0;
			double totalRate = 0;
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(new Integer(String.valueOf((eachWorker.getId()))));
			servicesWorkerVO.setCompanyName(eachWorker.getVendorAttrs().getVendorName());
			servicesWorkerVO.setHouseNumber(eachWorker.getVendorAttrs().getVendorOfficeAddress());
			servicesWorkerVO.setStreet(eachWorker.getBilling().getAddress());
			servicesWorkerVO.setArea(eachWorker.getArea());
			servicesWorkerVO.setCity(eachWorker.getBilling().getCity());
			servicesWorkerVO.setState(eachWorker.getBilling().getState());
			servicesWorkerVO.setPinCode(eachWorker.getBilling().getPostalCode());
			servicesWorkerVO.setContactNumber(eachWorker.getBilling().getTelephone());
			servicesWorkerVO.setImageUrl(eachWorker.getUserProfile());
			servicesWorkerVO.setCountry(eachWorker.getBilling().getCountry().getName());
			servicesWorkerVO.setShortDescription(eachWorker.getVendorAttrs().getVendorShortDescription());
			servicesWorkerVO.setDescription(eachWorker.getVendorAttrs().getVendorDescription());
			List<VendorRating> vendorRatingList = vendorRatingService.getVendorReviews(eachWorker.getId());
			if(vendorRatingList != null) {
				totalReviews = vendorRatingList.size();
				for(VendorRating vendorRating:vendorRatingList){
					totalRating= totalRating + vendorRating.getRating();
				}
				totalRate = totalRating;
				avgRating = Double.valueOf(totalRate / totalReviews);
				avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			}
			servicesWorkerVO.setAvgRating(avgRating);
			servicesWorkerVO.setTotalRating(totalReviews);
			servicesWorkerVOList.add(servicesWorkerVO);
		}
		LOGGER.debug("Ended findByVendorType");
		return servicesWorkerVOList;
	}

	@Override
	public List<Customer> getActivatedVendors() {
		return customerRepository.getActivatedVendors();
	}

	@Override
	public List<Customer> getAllVendors() {
		return customerRepository.getAllVendors();
	}

	@Override
	public List<Customer> getVendorsBasedOnStatus(String status) {
		return customerRepository.getVendorsBasedOnStatus(status);
	}
	
	@Override
	public List<Customer> getWallPaperVendorsByRating(BigDecimal rating,String vendorType) {
		return customerRepository.findWallPaperVendorsByRating(rating,vendorType);
	}

	@Override
	public List<Customer> getVendorsByLocation(String customerType,String searchString) {
		return customerRepository.findVendorsByLocation(customerType,searchString);
	}

	@Override
	public List<Customer> getVendorsByCustomerType(String customerType) {
		return customerRepository.findVendorsByCustomerType(customerType);
	}

	@Override
	public List<Customer> getVendorsBasedOnStatusAndCustomerType(String status, String customerType) {
		return customerRepository.findVendorsBasedOnStatusAndCustomerType(status,customerType);
	}

	@Override
	public List<Customer> getServiceProvidersByLocation(String customerType,String searchString) {
		return customerRepository.findServiceProvidersByLocation(customerType,searchString);
	}

	@Override
	public List<ServicesWorkerVO> getVendorsByCode(String code) {
		LOGGER.debug("Entered findByServiceType");
		WorkerServiceResponse response = new WorkerServiceResponse();
		List<Customer> customer = customerRepository.findVendorsByType(code);
		List<ServicesWorkerVO> servicesWorkerVOSet= new ArrayList<ServicesWorkerVO>();
		for(Customer eachWorker : customer){
			Double avgRating = new Double(0);
			int totalRating= 0;
			int totalReviews = 0;
			double totalRate = 0;
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(new Integer(String.valueOf((eachWorker.getId()))));
			servicesWorkerVO.setCompanyName(eachWorker.getVendorAttrs().getVendorName());
			servicesWorkerVO.setImageUrl(eachWorker.getUserProfile());
			servicesWorkerVO.setDescription(eachWorker.getVendorAttrs().getVendorDescription());
			servicesWorkerVO.setShortDescription(eachWorker.getVendorAttrs().getVendorShortDescription());
			/*servicesWorkerVO.setHouseNumber(eachWorker.getVendorAttrs().getVendorOfficeAddress());
			servicesWorkerVO.setStreet(eachWorker.getBilling().getAddress());
			servicesWorkerVO.setArea(eachWorker.getArea());
			servicesWorkerVO.setCity(eachWorker.getBilling().getCity());
			servicesWorkerVO.setState(eachWorker.getBilling().getState());
			servicesWorkerVO.setPinCode(eachWorker.getBilling().getPostalCode());
			servicesWorkerVO.setContactNumber(eachWorker.getBilling().getTelephone());
			servicesWorkerVO.setImageUrl(eachWorker.getVendorAttrs().getVendorAuthCert());
			servicesWorkerVO.setCountry(eachWorker.getBilling().getCountry().getName());*/
			//fetching ratings from services rating
			/*List<ServicesRating> servicesRatingList = servicesRatingService.getServicesReviews(eachWorker.getId());
			if(servicesRatingList != null) {
				totalReviews = servicesRatingList.size();
				for(ServicesRating servicesRating:servicesRatingList){
					totalRating= totalRating + servicesRating.getRating();
				}
				totalRate = totalRating;
				avgRating = Double.valueOf(totalRate / totalReviews);
				avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			}*/
			List<VendorRating> vendorRatingList = vendorRatingService.getVendorReviews(eachWorker.getId());
			totalReviews = vendorRatingList.size();
		  	for(VendorRating vendorRating:vendorRatingList) {
	    		
	    		totalRating = totalRating + vendorRating.getRating();
	    	}
	    	
			totalRate = totalRating;
			avgRating = Double.valueOf(totalRate / totalReviews);
			avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			
			/*eachWorker.setAvgReview(new BigDecimal(avgRating));
			try {
				this.saveOrUpdate(eachWorker);
			} catch (ServiceException e) {
				e.printStackTrace();
				LOGGER.error("Error in upadating vendor average rating "+e.getMessage());
			}*/
			servicesWorkerVO.setAvgRating(avgRating);
			//servicesWorkerVO.setTotalRating(totalRating);
			servicesWorkerVO.setTotalRating(totalReviews);
			servicesWorkerVOSet.add(servicesWorkerVO);
		}
		LOGGER.debug("Ended findByVendorType");
		return servicesWorkerVOSet;
	}

	@Override
	public List<Customer> getVendorsByLocationAndSubCategory(String customerType, String searchString,
			String searchSubCategory) {
		return customerRepository.findVendorsByLocationAndSubCategory(customerType,searchString,searchSubCategory);
	}

	@Override
	public List<Customer> getVendorsBasedOnSubCategoryByRating(BigDecimal rating, String vendorType,
			String searchSubCategory) {
		return customerRepository.findVendorsBasedOnSubCategoryByRating(rating,vendorType,searchSubCategory);
	}

	@Override
	public List<Customer> getPaidOrUnPaidVendorsBasedOnStatus(String status) {
		return customerRepository.findPaidOrUnPaidVendorsBasedOnStatus(status);
	}

	@Override
	public List<Customer> getPaidOrUnPaidVendorsBasedOnStatusAndCustomerType(String status, String customerType) {
		return customerRepository.getPaidOrUnPaidVendorsBasedOnStatusAndCustomerType(status,customerType);
	}

	@Override
	public List<Customer> getPaidOrUnPaidtVendorsByCustomerType(String customerType) {
		return customerRepository.gePaidOrUnPaidtVendorsByCustomerType(customerType);
	}

	@Override
	public List<Customer> getAllPaidOrUnPaidVendors() {
		return customerRepository.getAllPaidOrUnPaidVendors();
	}

	@Override
	public List<Customer> getVendorSearchByName(String searchString) {
		return customerRepository.getVendorSearchByName(searchString);
	}

	@Override
	public List<Customer> getVendorSearchById(Long vendorId) {
		return customerRepository.getVendorSearchById(vendorId);
	}

	@Override
	public List<Customer> searchPaidOrUnPaidVendorsByName(String searchString) {
		return customerRepository.searchPaidOrUnPaidVendorsByName(searchString);
	}

	@Override
	public List<Customer> searchPaidOrUnPaidVendorsById(Long vendorId) {
		return customerRepository.searchPaidOrUnPaidVendorsById(vendorId);
	}

	@Override
	public List<ServicesWorkerVO> findByServiceTypeAndRated(String serviceType) {
		LOGGER.debug("Entered findByServiceTypeAndRated");
		WorkerServiceResponse response = new WorkerServiceResponse();
		List<Customer> customer = customerRepository.findByServiceTypeAndRated(serviceType);
		List<ServicesWorkerVO> servicesWorkerVOSet= new ArrayList<ServicesWorkerVO>();
		List<ServicesWorkerVO> serviceWorkersList = null;
		for(Customer eachWorker : customer){
			Double avgRating = new Double(0);
			int totalRating= 0;
			int totalReviews = 0;
			double totalRate = 0;
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(new Integer(String.valueOf((eachWorker.getId()))));
			servicesWorkerVO.setCompanyName(eachWorker.getVendorAttrs().getVendorName());
			servicesWorkerVO.setImageUrl(eachWorker.getUserProfile());
			/*servicesWorkerVO.setHouseNumber(eachWorker.getVendorAttrs().getVendorOfficeAddress());
			servicesWorkerVO.setStreet(eachWorker.getBilling().getAddress());
			servicesWorkerVO.setArea(eachWorker.getArea());
			servicesWorkerVO.setCity(eachWorker.getBilling().getCity());
			servicesWorkerVO.setState(eachWorker.getBilling().getState());
			servicesWorkerVO.setPinCode(eachWorker.getBilling().getPostalCode());
			servicesWorkerVO.setContactNumber(eachWorker.getBilling().getTelephone());
			servicesWorkerVO.setImageUrl(eachWorker.getVendorAttrs().getVendorAuthCert());
			servicesWorkerVO.setCountry(eachWorker.getBilling().getCountry().getName());*/
			//fetching ratings from services rating
			List<ServicesRating> servicesRatingList = servicesRatingService.getServicesReviews(eachWorker.getId());
			if(servicesRatingList != null) {
				totalReviews = servicesRatingList.size();
				for(ServicesRating servicesRating:servicesRatingList){
					totalRating= totalRating + servicesRating.getRating();
				}
				totalRate = totalRating;
				avgRating = Double.valueOf(totalRate / totalReviews);
				avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			}
			servicesWorkerVO.setAvgRating(avgRating);
			//servicesWorkerVO.setTotalRating(totalRating);
			servicesWorkerVO.setTotalRating(totalReviews);
			servicesWorkerVOSet.add(servicesWorkerVO);
			
			serviceWorkersList = new ArrayList<ServicesWorkerVO>(servicesWorkerVOSet);
			Collections.sort(serviceWorkersList, Collections.reverseOrder(new ServiceWorkersDescRatingComparator()));
		}
		LOGGER.debug("Ended findByServiceTypeAndRated");
		return serviceWorkersList;
	}


}
