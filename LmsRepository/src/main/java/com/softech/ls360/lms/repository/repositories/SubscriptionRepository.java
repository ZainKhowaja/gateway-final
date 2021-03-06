package com.softech.ls360.lms.repository.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

import com.softech.ls360.lms.repository.entities.Subscription;
import com.softech.ls360.lms.repository.entities.VU360User;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long>{

	
	List<Subscription> findByVu360User_usernameAndSubscriptionStatus(String userName, String subscriptionStatus);
	//List<Subscription> findByIdAndSubscriptionStatusAndOrderStatus(String userName, String subscriptionStatus, String orderStatus);
/*	@Query(value = " SELECT S.ID, isnull(CE.ORDERSTATUS, '') FROM SUBSCRIPTION S " +
			" inner join customerentitlement ce on s.CUSTOMERENTITLEMENT_ID = ce.id " +
			" where s.id = ?1 ", nativeQuery = true)
	List<Object[]> findSubscriptionOrderStatus(Long id);*/
	
	@Query(value = " SELECT S.ID, isnull(CE.ORDERSTATUS, ''), s.SUBSCRIPTION_NAME FROM SUBSCRIPTION S " +
			" inner join customerentitlement ce on s.CUSTOMERENTITLEMENT_ID = ce.id " +
			" where s.subscription_code = ?1 ", nativeQuery = true)
	List<Object[]> findSubscriptionOrderStatus(String code);
	
	Subscription findBySubscriptionCode(String subscriptionCode);
	Subscription findBySubscriptionCodeAndSubscriptionStatus(String subscriptionCode, String subscriptionStatus);
	
	@Query(value = " select sk.* from SUBSCRIPTION_KIT sk " +
			" inner join SUBSCRIPTION s on sk.id = s.SUBSCRIPTION_KIT_ID  " +
			" inner join SUBSCRIPTION_USER su on s.id=su.SUBSCRIPTION_ID " +
			" inner join vu360user u on u.id = su.vu360user_id " +
			" where u.username=?1 ", nativeQuery = true)
	List<Object[]> findSubscriptionNameByUsername(String username);
	
	List<Subscription> findByCustomerEntitlement_Customer_id(Long customerId);
}
