package com.softech.ls360.api.gateway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.ls360.api.gateway.service.CustomerService;
import com.softech.ls360.api.gateway.service.UserService;
import com.softech.ls360.lms.repository.entities.CustomField;
import com.softech.ls360.lms.repository.entities.Customer;
import com.softech.ls360.lms.repository.entities.VU360User;
import com.softech.ls360.lms.repository.projection.customer.CustomerCustomFields;
import com.softech.ls360.lms.repository.repositories.CustomerRepository;
import com.softech.vu360.lms.webservice.message.lmsapi.types.securityroles.Vu360Users;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Inject
	private CustomerRepository customerRepository;
	
	@Inject
	private UserService userService;
	@Override
	public Set<CustomField> findCustomerCustomFields(Long customerId) {
		Set<CustomField> customFields = null;
		Optional<List<CustomerCustomFields>> optionalCustomerCustomFields = customerRepository.findCustomFieldsById(customerId);
		if (optionalCustomerCustomFields.isPresent()) {
			List<CustomerCustomFields> customerCustomFields = optionalCustomerCustomFields.get();
			if (!CollectionUtils.isEmpty(customerCustomFields)) {
				customFields = customerCustomFields
						.stream()
						.map(CustomerCustomFields::getCustomFields)
						.collect(Collectors.toSet());
			}
		}
		return customFields;
	}
	
	public Customer findByUsername(String username){
		List<Customer> lstCustomer = customerRepository.findByUsername(username);
		if (lstCustomer!=null && lstCustomer.size()>0)
			return lstCustomer.get(0);
		else
			return null;
	}

	public List<Object[]> findEntitlementByCustomer(Long customerId){
		List<Object[]> objCE = customerRepository.findEntitlementByCustomer(customerId);
		return objCE;
	}
	
	
	public List<Object[]> getEnrollmentsDetail(Long customerId){
		List<Object[]> lstED = customerRepository.getEnrollmentsDetail(customerId);
		
		 return lstED;
	}
	
	public List<Object[]> getLearnersByCustomer(Long customerId){
		List<Object[]> lstED = customerRepository.getLearnersByCustomer(customerId);
		
		 return lstED;
	}
	
	@Override
	public List<Object[]> getCustomerIdByOrderId(String orderId){
		List<Object[]> arrCusomerId = customerRepository.getCustomerIdByOrderId(orderId);
		if(arrCusomerId==null)
			return null;
		else
			return arrCusomerId;
	}
	
	@Override
	public boolean updateOrderStatusByCustomerentitlementId(String status, Long entitlementId){
		if(entitlementId!=null && entitlementId>0){
			if(status.equals("canceled")){
				 customerRepository.updateCustomerentitlementStatus(status, entitlementId);
				 customerRepository.updateEnrollmentAndOrderStatus("Dropped", status, entitlementId);
			}else{
				 customerRepository.updateCustomerentitlementStatus(status, entitlementId);
				 customerRepository.updateEnrollmentOrderStatus(status, entitlementId);
			}
		}
		 return true;
	}
	
	public List getVPAOrdersByCustomer(String vpaCode, Long customerId, String managerusername, String teamId){
		
		VU360User objUsers = userService.findByUsername(managerusername);
		List<Object[]> objResult;
		
		if(!teamId.equals("")){
			objResult  = customerRepository.getVPAOrdersByCustomerByTeam(customerId, vpaCode, teamId);
		}else{	
			objResult  = customerRepository.getVPAOrdersByCustomer(customerId, vpaCode);
		}	
		Map<String, String> colmap;
		
		List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
		
		for(Object[]  orderInfo : objResult){
			colmap = new HashMap<String, String>();
			
			if(orderInfo[1]!=null)
				colmap.put("orderId", orderInfo[1].toString());
			
			if(orderInfo[3]!=null)
				colmap.put("firstName", orderInfo[3].toString());
			
			if(orderInfo[4]!=null)
				colmap.put("lastName", orderInfo[4].toString());
			
			if(orderInfo[5]!=null)
				colmap.put("email", orderInfo[5].toString());
			
			if(orderInfo[6]!=null)
				colmap.put("courseName", orderInfo[6].toString());
			
			if(orderInfo[7]!=null)
				colmap.put("orderDate", orderInfo[7].toString());
			
			if(orderInfo[8]!=null)
				colmap.put("guid", orderInfo[8].toString());
			
			colmap.put("ClassDate", "");
			lst.add( colmap );
		}
		

		if(teamId.equals("")){
			List<Object[]> objResultSubCount  = customerRepository.getVPAOrdersByCustomerForSubscriptionCount(customerId, vpaCode);
			for(Object[]  orderInfo : objResultSubCount){
				int count  = Integer.parseInt(orderInfo[2].toString());
				
				for(int i=1; i<=count;i++){
					colmap = new HashMap<String, String>();
					
					if(orderInfo[1]!=null)
						colmap.put("orderId", orderInfo[1].toString());
					
					colmap.put("firstName", objUsers.getFirstName());
					colmap.put("lastName", objUsers.getLastName());
					colmap.put("email", objUsers.getEmailAddress());
					
					if(orderInfo[3]!=null)
						colmap.put("courseName", orderInfo[3].toString());
					
					if(orderInfo[4]!=null)
						colmap.put("orderDate", orderInfo[4].toString());
					
					if(orderInfo[5]!=null)
						colmap.put("guid", orderInfo[5].toString());
					
					colmap.put("ClassDate", "");
					lst.add( colmap );
				}
			}
		}
		
	
		
		// courses -------------------------------------------------------------------------------------------------
		List<Object[]> objResultCourse;
		if(!teamId.equals("")){
			objResultCourse = customerRepository.getVPAOrdersByCustomerForCourseByTeam(customerId, vpaCode, teamId);
		}else{
			objResultCourse  = customerRepository.getVPAOrdersByCustomerForCourse(customerId, vpaCode);
		}
		
		for(Object[]  orderInfo : objResultCourse){
			colmap = new HashMap<String, String>();
			
			if(orderInfo[6]!=null)
				colmap.put("orderId", orderInfo[6].toString());
			if(orderInfo[1]!=null)
				colmap.put("firstName", orderInfo[1].toString());
			if(orderInfo[2]!=null)
				colmap.put("lastName", orderInfo[2].toString());
			if(orderInfo[3]!=null)
				colmap.put("email", orderInfo[3].toString());
			if(orderInfo[4]!=null)
				colmap.put("courseName", orderInfo[4].toString());
			if(orderInfo[5]!=null)
				colmap.put("orderDate", orderInfo[5].toString());
			
			if(orderInfo[7]!=null)
				colmap.put("guid", orderInfo[7].toString());
			
			if(orderInfo[8] != null && orderInfo[9]!=null)
				colmap.put("ClassDate", orderInfo[8].toString() + " - " + orderInfo[9].toString());
			else
				colmap.put("ClassDate", "");
			
			lst.add( colmap );
		}
		
		if(teamId.equals("")){
			List<Object[]> objResultCourseCount  = customerRepository.getVPAOrdersByCustomerForCourseCount(customerId, vpaCode);
			for(Object[]  orderInfo : objResultCourseCount){
				int count  = Integer.parseInt(orderInfo[2].toString());
				
				for(int i=1; i<=count;i++){
					colmap = new HashMap<String, String>();
					
					if(orderInfo[1]!=null)
						colmap.put("orderId", orderInfo[1].toString());
					
					colmap.put("firstName", objUsers.getFirstName());
					colmap.put("lastName", objUsers.getLastName());
					colmap.put("email", objUsers.getEmailAddress());
					
					if(orderInfo[3]!=null)
						colmap.put("courseName", orderInfo[3].toString());
					
					if(orderInfo[4]!=null)
						colmap.put("orderDate", orderInfo[4].toString());
					
					if(orderInfo[5]!=null)
						colmap.put("guid", orderInfo[5].toString());
					
					colmap.put("ClassDate", "");
					lst.add( colmap );
				}
			}
		}
		
		return lst;
	}
	
	
	
	
}
