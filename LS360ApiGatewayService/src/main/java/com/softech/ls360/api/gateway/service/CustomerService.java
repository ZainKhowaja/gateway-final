package com.softech.ls360.api.gateway.service;

import java.util.List;
import java.util.Set;

import com.softech.ls360.api.gateway.service.model.request.LearnerRequest;
import com.softech.ls360.lms.repository.entities.CustomField;
import com.softech.ls360.lms.repository.entities.Customer;

public interface CustomerService {
	Set<CustomField> findCustomerCustomFields(Long customerId);
	Customer findByUsername(String username);
	List<Object[]> findEntitlementByCustomer(Long customerId);
	List<Object[]> getEnrollmentsDetail(Long customerId);
	
	List<Object[]> getLearnersByCustomer(Long customerId);
	
	List<Object[]> getCustomerIdByOrderId(String orderId);
	boolean updateOrderStatusByCustomerentitlementId(String status, Long entitlementId);
	
	List getVPAOrdersByCustomer(String vpaCode, Long customerId, String managerusername, String teamId);
}
