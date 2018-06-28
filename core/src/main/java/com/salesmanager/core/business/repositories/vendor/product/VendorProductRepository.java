package com.salesmanager.core.business.repositories.vendor.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{
	
/*	@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  join fetch pd.attributes attr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where customer.id=?1")
*/	@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  left join fetch pd.attributes pattr join fetch  pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.vendorWishListed = FALSE and customer.id=?1")
	public List<VendorProduct> findProductsByVendor(Long vendorId);

	@Query("select distinct vp from VendorProduct vp  left join fetch vp.product pd "
			+ " join fetch pd.images img left join fetch pd.attributes pattr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.vendorWishListed = TRUE  and vp.customer.id=?1 group by pd.id")
	public List<VendorProduct> findProductWishListByVendor(Long vendorId);

	public List<VendorProduct> findVendorById(Long vendorId);

	/*@Query("select vp from VendorProduct vp join fetch vp.product pd "
			+ "join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.adminActivated = FALSE ")*/
	@Query("select distinct vp from VendorProduct vp  left join fetch vp.product pd "
			+ " join fetch pd.images img left join fetch pd.attributes pattr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.adminActivated = FALSE ")
	public List<VendorProduct> findVendorProducts();

	@Query("select distinct vp from VendorProduct vp join fetch vp.product pd join fetch vp.customer customer"
			+ " where vp.adminActivated = TRUE and pd.id=?1 ")
	public List<VendorProduct> findProductVendors(Long productId);

	@Query("select distinct vp from VendorProduct vp join fetch vp.product pd join fetch vp.customer customer"
			+ " where vp.adminActivated = TRUE and pd.id=?1 and customer.billing.postalCode = ?2 and customer.isVendorActivated='Y'")
	public List<VendorProduct> findProductVendorsByProductIdAndCustomerPinCode(Long productId,String postalCode);

	@Query("select distinct vp.customer from VendorProduct vp where vp.customer.customerType != '0'")
	public List<Customer> getRequestedVendors();

	/*@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  left join fetch pd.attributes pattr join fetch  pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where customer.id=?1")*/
	@Query("select distinct vp from VendorProduct vp join fetch  vp.customer customer where customer.id=?1 and vp.vendorWishListed = FALSE")
	public List<VendorProduct> findVendorAddedProductsByVendorId(Long vendorId);

	@Query("select distinct vp from VendorProduct vp join fetch  vp.customer customer where vp.adminActivated = TRUE and customer.id=?1")
	public List<VendorProduct> getVendorApprovedProductsByVendorId(Long vendorId);

	@Query("select distinct vp from VendorProduct vp join fetch  vp.customer customer where vp.product.id = ?1 and vp.customer.id=?2 and vp.vendorWishListed = TRUE")
	public List<VendorProduct> getByProductIdAndVendorId(Long productId, Long vendorId);

	@Query("select distinct vp from VendorProduct vp join fetch  vp.customer customer where vp.product.id = ?1 and vp.customer.id=?2 and vp.vendorWishListed = FALSE")
	public List<VendorProduct> findVendoProductsByProductIdAndVendorId(Long longProductId, Long longVendorId);

	@Query("select distinct vp.customer from VendorProduct vp where vp.customer.customerType != '0' and vp.customer.vendorAttrs.vendorName like %?1%")
	public List<Customer> searchRequestedVendorsByName(String searchString);

	@Query("select distinct vp.customer from VendorProduct vp where vp.customer.customerType != '0' and vp.customer.id = ?1")
	public List<Customer> searchRequestedVendorsById(Long vendorId);
	

	
}
