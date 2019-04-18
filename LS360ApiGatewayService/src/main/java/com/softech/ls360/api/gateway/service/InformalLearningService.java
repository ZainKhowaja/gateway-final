package com.softech.ls360.api.gateway.service;



import java.util.HashMap;
import java.util.List;

import com.softech.ls360.lms.repository.entities.InformalLearning;
import com.softech.ls360.lms.repository.entities.InformalLearningActivity;

public interface InformalLearningService {
	public void logInformalLearning(InformalLearning informalLearning);
	public List<HashMap<String,Double>> getActivityTimeSpent(String username);
	public List<Object[]> getActivityTimeSpentByTopic(String userName);
	
	void logInformalLearningActivity(InformalLearningActivity informalLearningActivity);
	InformalLearningActivity getInformalLearningActivity(com.softech.ls360.api.gateway.service.model.request.InformalLearningActivityRequest infLearRequest);
	InformalLearningActivity findById(long id);
	boolean deleteInformalLearningActivity(long id);
}
