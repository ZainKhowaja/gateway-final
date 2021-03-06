package com.softech.ls360.api.gateway.endpoint.restful.manager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

import com.softech.ls360.api.gateway.request.GlobalBatchImportRestRequest;
import com.softech.ls360.api.gateway.service.MessageSenderService;
import com.softech.ls360.api.gateway.service.UserService;
import com.softech.ls360.api.gateway.service.model.request.GlobalBatchImportParamSerialized;
import com.softech.ls360.lms.repository.entities.BatchimportFailure;
import com.softech.ls360.lms.repository.entities.VU360User;



@ComponentScan
@RestController
@RequestMapping(value = "/lms/customer")
public class GlobalBatchImportRestEndPoint {
	
	@Inject
	MessageSenderService messageSenderService;
	
	@Inject
	UserService userService;
	
	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * @Desc :: This end point use batch import
	 */
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> batchImport(@RequestHeader("Authorization") String authorization, @RequestBody GlobalBatchImportRestRequest restRequest) {
		 Map<Object, Object> responseData = new HashMap<Object, Object>();
		 AsyncRestTemplate restTemplate = new AsyncRestTemplate();
		 String token = authorization.substring("Bearer".length()).trim();
		 HttpHeaders headers = new HttpHeaders();
		 headers.add("token", token);
		 headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());   
		 String username = SecurityContextHolder.getContext().getAuthentication().getName();
		 VU360User objUser = userService.findByUsername(username);
		 
		 logger.info("---In Batch import start >>>>>>>>>>>>>>>>>>>>>0" );
		 try {
			 	logger.info("---In Batch import start >>>>>>>>>>>>>>>>>>>>> 1" +  restRequest.getFilePath());
	            GlobalBatchImportParamSerialized obj =new GlobalBatchImportParamSerialized(objUser.getEmailAddress(), username, restRequest.getFilePath(),restRequest.getAction(), restRequest.getWebsiteId(), restRequest.getStoreId());
	            logger.info("---------------------------------------------------------------");
	            logger.info(username + " " + restRequest.getFilePath()+ " " + restRequest.getAction()+ " " + restRequest.getWebsiteId());
	            logger.info("---------------------------------------------------------------");
	            messageSenderService.sendMessage(obj);
	            logger.info("---In Batch import start >>>>>>>>>>>>>>>>>>>>> 2" );
	            responseData.put("status", Boolean.TRUE.toString());
	            responseData.put("message", "");
		        return responseData;
	        
	        }catch(UncategorizedJmsException e){
	        	logger.info("---In Batch import start >>>>>>>>>>>>>>>>>>>>> UncategorizedJmsException 3" + e.getMessage() );
	        	BatchimportFailure objBIF = new BatchimportFailure(restRequest.getFilePath(), restRequest.getAction(), objUser.getEmailAddress(), false, restRequest.getWebsiteId(), restRequest.getStoreId());
	        	messageSenderService.saveBatchimportFailure(objBIF);
	        	responseData.put("status", Boolean.FALSE.toString());
	        	responseData.put("message", "ActiveMQ is not working. Record is saved into database");
	        	return responseData;
	        }catch(Exception e){
	        	logger.info("---In Batch import start >>>>>>>>>>>>>>>>>>>>> 4" + e.getMessage());
	        	responseData.put("status", Boolean.FALSE.toString());
	        	return responseData;
	        }
	}
	
}
