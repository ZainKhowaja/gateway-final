package com.softech.ls360.api.gateway.service;

import com.softech.ls360.lms.repository.entities.SubscriptionKit;

public interface SubscriptionKitService {
		
	 public boolean findBydGuid(String guid);
	 public SubscriptionKit addSubscriptionKit(SubscriptionKit subscriptionKit);
	 
}
