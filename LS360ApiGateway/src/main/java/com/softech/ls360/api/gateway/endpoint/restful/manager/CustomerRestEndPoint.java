package com.softech.ls360.api.gateway.endpoint.restful.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.softech.ls360.api.gateway.config.spring.annotation.RestEndpoint;
import com.softech.ls360.api.gateway.request.UserRequest;
import com.softech.ls360.api.gateway.response.UserCourseAnalyticsResponse;
import com.softech.ls360.api.gateway.service.CustomerService;
import com.softech.ls360.api.gateway.service.LearnerService;
import com.softech.ls360.api.gateway.service.LmsRoleLmsFeatureService;
import com.softech.ls360.api.gateway.service.StatisticsService;
import com.softech.ls360.api.gateway.service.UserService;
import com.softech.ls360.api.gateway.service.model.request.CourseDetail;
import com.softech.ls360.api.gateway.service.model.request.LearnerRequest;
import com.softech.ls360.api.gateway.service.model.vo.EnrollmentDetailVO;
import com.softech.ls360.api.gateway.service.model.vo.VU360UserVO;
import com.softech.ls360.lms.api.model.request.UserPermissionRequest;
import com.softech.ls360.lms.repository.entities.Customer;
import com.softech.ls360.lms.repository.entities.Learner;
import com.softech.ls360.lms.repository.entities.LmsRole;
import com.softech.ls360.lms.repository.entities.VU360User;
import com.softech.vu360.lms.webservice.message.lmsapi.serviceoperations.user.AddUserResponse;

@RestEndpoint
@RequestMapping(value="/lms/customer")
public class CustomerRestEndPoint {

	@Autowired
	private Environment env;
	
	@Inject
	private LearnerService learnerService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private StatisticsService statsService;
	
	@Inject
	private CustomerService customerService;
	
	@Inject
	private LmsRoleLmsFeatureService lmsRoleLmsFeatureService;
	
	@RequestMapping(value = "/useranalytics", method = RequestMethod.POST)
	@ResponseBody
	public Map<Object, Object> getUserAnalytics(@RequestBody UserRequest userRequest) throws Exception {
		Long totalViewTime = 0L;
    	Long activeDays=0L;
    	String lastLogin =null;
    	String startDate = null;
    	List<String> courses = new ArrayList<String>();
    	List<String> completeCourse =new ArrayList<String>();;
    	UserCourseAnalyticsResponse objuca = new UserCourseAnalyticsResponse();
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		List<Object[]> UserCourseAnalytics = learnerService.findUserCourseAnalyticsByUserName(userRequest.getUsername());
        for(Object[]  objCE : UserCourseAnalytics){
        	try{
        		if(objCE[5].toString()=="true")
    				completeCourse.add(objCE[4].toString());
    			else
    				courses.add(objCE[4].toString());
        		
        		if(objCE[0]!=null)
        			totalViewTime += Long.valueOf(objCE[0].toString());
	        	
        		if(objCE[1]!=null)
        			activeDays += Long.valueOf(objCE[1].toString());
        		
        	}catch(Exception ex){
        		
        	}
        }
        
        if(UserCourseAnalytics!=null && UserCourseAnalytics.size()>0){
        	Object[]  objCE2 = UserCourseAnalytics.get(0);
        	if(objCE2[3]!=null)
    			startDate = objCE2[3].toString();
        	
        	if(objCE2[2]!=null)
        		lastLogin = objCE2[2].toString();
        }
        
        objuca.setCourses(courses);
        objuca.setCompleteCourse(completeCourse);
        objuca.setTotalViewTime(totalViewTime);
        objuca.setActiveDays(activeDays);
        objuca.setLastLogin(lastLogin); 
        objuca.setStartDate(startDate); 
        
        
        // set team name
        String learnerGroup = learnerService.findLearnerGroupByUsername(userRequest.getUsername());
        
        try{
	        if(learnerGroup!=null)
	        	objuca.setTeamName(learnerGroup);
	        else
	        	objuca.setTeamName("__default");
        }catch(Exception ex){
        	objuca.setTeamName("__default");
    	}
        
        
        // get user enrolled subscription name
        List subname = new ArrayList();
        List<Object[]> lstSub = learnerService.findSubscriptionNameByUsername(userRequest.getUsername());
        for(Object[]  objCE : lstSub){
        	try{
        		if(objCE[1]!=null)
        			subname.add(objCE[1].toString());
        	}catch(Exception ex){}
        }
        objuca.setSubscriptions(subname);
        
        Long AverageViewTime = statsService.getAverageViewTimeByWeekByUserName(userRequest.getUsername());
        objuca.setAverageViewTimeByWeek(AverageViewTime);
        map.put("status", Boolean.TRUE);
        map.put("result", objuca);
		return map;
	}

	
	
	@RequestMapping(value = "/validateHierarchy", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> validateHierarchy(@RequestBody Map<String, Object> request) throws Exception {
		Map<String, Object> colmap = new HashMap<String, Object>();
		Map<String, Object> colmap2 = new HashMap<String, Object>();
		
		String customerUsername = request.get("customerUsername").toString();
		String learnerUsername = request.get("learnerUsername").toString();
		
		if((customerUsername == null || customerUsername.equals("")) 
				&& (learnerUsername == null || learnerUsername.equals("")) ){
			colmap.put("status", Boolean.FALSE);
			colmap.put("message", "Data was Incorrect");
			return colmap;
		}
		
		Customer customer = customerService.findByUsername(customerUsername);
		Customer customer2 = customerService.findByUsername(learnerUsername);
		
		
		if(customer == null && customer2 == null ){
			colmap.put("status", Boolean.FALSE);
			colmap.put("message", "Data was Incorrect");
			return colmap;
		}
		
		boolean isInSameHirarchi = false, possibleOrgChange = true;
		
		if(customer.getId().longValue()==customer2.getId().longValue()){
			isInSameHirarchi = true;
		}
		
		List<String> lstRoleType = lmsRoleLmsFeatureService.getRoleTypesByUsername(learnerUsername);
		
		for(String role : lstRoleType){
			if(!role.equalsIgnoreCase("ROLE_LEARNER")){
				possibleOrgChange = false;
			}
		}
		
		if(isInSameHirarchi || possibleOrgChange)
			colmap2.put("allowCheckout", Boolean.TRUE );
		else //if(isInSameHirarchi && possibleOrgChange)
			colmap2.put("allowCheckout", Boolean.FALSE );
		
		
		if(possibleOrgChange && !isInSameHirarchi)
			colmap2.put("orgChange", Boolean.TRUE );
		else //if(isInSameHirarchi && possibleOrgChange)
			colmap2.put("orgChange", Boolean.FALSE );
		
		colmap.put("status", Boolean.TRUE);
		colmap.put("message", "");
		colmap.put("result", colmap2);
		return colmap;
	}
	
	
	@RequestMapping(value = "/org-change", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeLearnerHierarchy(@RequestBody Map<String, Object> request) throws Exception {
		HashMap<String,Object> response = new HashMap<String,Object>();
		RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
        //headers.add("token", token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());

        HttpEntity requestData = new HttpEntity(request, headers);

        StringBuffer location = new StringBuffer();
        location.append(env.getProperty("lms.baseURL").trim()).append("rest/user/org-change");
       // logger.info("---User End Point >>>>>>>>>>>>>>>>>>>>>0" + location.toString());
       
        ResponseEntity<Map> returnedData = restTemplate.postForEntity(location.toString(), requestData, Map.class);
        response = (HashMap<String, Object>) returnedData.getBody();
       
        return response;
	}
	
	@RequestMapping(value = "/order-status", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateOrderStatus(@RequestBody Map<String, Object> request) throws Exception {
		Map<String, Object> colmap = new HashMap<String, Object>();
		
		if(request.get("orderId")==null && request.get("email")==null && request.get("action")==null){
			colmap.put("status", Boolean.FALSE);
			colmap.put("message", "Data was Incorrect");
			return colmap;
		}
		
		String usreName = request.get("email").toString();
		String orderId = request.get("orderId").toString();
		String action = request.get("action").toString();
		
		Learner objLearner = learnerService.findByVu360UserUsername(usreName);
		List<Object[]> arrOrder = customerService.getCustomerIdByOrderId(orderId);
		
		Long entitId=0l;
		Long customerId=0l;
				
		for(Object[]  orderInfo : arrOrder){
			customerId = Long.valueOf(orderInfo[0].toString());
			break;
		}
		
		if(objLearner.getCustomer().getId().equals(customerId) && action.equalsIgnoreCase("completed")){
			for(Object[]  orderInfo : arrOrder){
				entitId = Long.valueOf(orderInfo[1].toString());
				if(entitId!=null && entitId>0)
					customerService.updateOrderStatusByCustomerentitlementId("completed", entitId);
			}
		}else if(objLearner.getCustomer().getId().equals(customerId) && action.equalsIgnoreCase("canceled")){
			for(Object[]  orderInfo : arrOrder){
				entitId = Long.valueOf(orderInfo[1].toString());
				if(entitId!=null && entitId>0)
					customerService.updateOrderStatusByCustomerentitlementId("canceled", entitId);
			}
		}
		
		colmap.put("status", Boolean.TRUE);
		colmap.put("message", "Order marked complete successfully! ");
		colmap.put("result", null);
		return colmap;
	}
//
//	@RequestMapping(value = "/useranalyticsByCourse", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<Object, Object> getUserAnalytics2(@RequestBody UserRequest userRequest) throws Exception {
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		List<Object[]> UserCourseAnalytics = learnerService.findUserCourseAnalyticsByUserNameByCourseGUIDs(userRequest.getCourseguid());
//		int totalViewTime = 0;
//		int completedCount =0;
//		for(Object[]  objCE : UserCourseAnalytics){
//        	try{
//        		if(objCE[0]!=null)
//        			totalViewTime += Long.valueOf(objCE[0].toString());
//	        	
//        		if((objCE[1]!=null && objCE[1].toString().equals("1"))  &&   (objCE[2] !=null && objCE[2].toString().equalsIgnoreCase("completed")))
//        			completedCount += completedCount;
//        		
//        	}catch(Exception ex){
//        		
//        	}
//        }
//		
//		UserAnalyticsResponse objuca = new UserAnalyticsResponse();
//		objuca.setTotalViewTime(totalViewTime);
//		objuca.setCompleteCourse(completedCount);
//		
//        map.put("status", Boolean.TRUE);
//        map.put("result", objuca);
//		return map;
//	}

	
	
	/**
	 * @Desc :: This end point use update User statuses like locked/unlocked and/or disabled/enable
	 */
	@RequestMapping(value = "/user/permission", method=RequestMethod.PUT)
	@ResponseBody
	public  Map<Object, Object> changePermission(@RequestBody UserPermissionRequest ObjUserPermissionReq) {
		 List<VU360UserVO> vU360UserVO =new ArrayList<VU360UserVO>();
		 for(String username : ObjUserPermissionReq.getUserName()){
			 VU360UserVO objuser = new VU360UserVO();
			 objuser.setUsername(username);
			 if(ObjUserPermissionReq.getEnabled()!=null)
				 objuser.setEnabled(ObjUserPermissionReq.getEnabled());
			 
			 if(ObjUserPermissionReq.getLocked()!=null)
				 objuser.setLocked(!ObjUserPermissionReq.getLocked());
			 vU360UserVO.add(objuser);
		 }
		 
		 userService.changePermission(vU360UserVO);
		 Map<Object, Object> map = new HashMap<Object, Object>();
		 map.put("status", Boolean.TRUE);
		 map.put("message", "User Status changed");
		 return map;
	}
	
	
//	@RequestMapping(value = "enrollmentsDetail", method = RequestMethod.GET)
//	@ResponseBody
//	public Map<Object, Object> getEnrollmentsDetail() throws Exception {
//		
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        Customer customer = customerService.findByUsername(userName);
//        List<Object[]> lstED = customerService.getEnrollmentsDetail(customer.getId());
//        
//        
//        
//        
//        List<LearnerRequest> lstenrollmentDetail = new ArrayList<LearnerRequest>();
//		LearnerRequest objLearner=null ;;
//		Map<Object, Object> enrollmentsDetail = new HashMap<Object, Object>();
//		boolean flag = false;
//		long learnerID=-1;
////		if(lstED.size()>0){
////			Object[]  objCE1 = lstED.get(0);
////			learnerID = Long.valueOf(objCE1.toString());
////		}
//		System.out.println("sssssssssssssssssssssssssssssssssss");
//		 for(Object[]  objCE : lstED){
//	        	try{
//	        		if(learnerID==-1 || learnerID!=Long.valueOf(objCE[0].toString())){
//	        			learnerID = Long.valueOf(objCE[0].toString());
//	        			Object[]  objCE1 = lstED.get(0);
//	        			learnerID = Long.valueOf(objCE[0].toString());
//	        			enrollmentsDetail = new HashMap<Object, Object>();
//	        			
//	        			objLearner = new LearnerRequest();
//		        		objLearner.setFirstName(objCE[1].toString());
//		        		objLearner.setLastName(objCE[2].toString());
//		        		objLearner.setEmail(objCE[3].toString());
//		        		flag = true;
//	        		}
//	        		
//	        		
//	        		
//	        		
//	        		enrollmentsDetail = objLearner.getEnrollmentsDetail();
//	        		enrollmentsDetail.put("courseName", objCE[4].toString());
//	        		enrollmentsDetail.put("coursetype", objCE[5].toString());
//	        		enrollmentsDetail.put("courseStatus", objCE[6].toString());
//	        		
//	        		
//	        		objLearner.setEnrollmentsDetail(enrollmentsDetail);
//	        		
//	        		if(flag){
//	        			lstenrollmentDetail.add(objLearner);
//	        			flag=false;
//	        		}
//	        	}catch(Exception ex){
//	        		//logger.error(">>> Exception occurs while send the organizationgroupdetail >>>: findEntitlementByCustomer(customer.getId()) >> " + ex);
//	        	}
//	        	
//		 }
//        
//        map.put("result", lstenrollmentDetail);
//        map.put("status", Boolean.TRUE);
//        map.put("message", "Success");
//        return map;
//        
//	}
	
	@RequestMapping(value = "enrollmentsDetail", method = RequestMethod.GET)
	@ResponseBody
	public Map<Object, Object> getEnrollmentsDetail() throws Exception {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.findByUsername(userName);
        List<Object[]> lstED = customerService.getLearnersByCustomer(customer.getId());
        
        
        
        
        List<LearnerRequest> lstenrollmentDetail = new ArrayList<LearnerRequest>();
		LearnerRequest objLearner=null ;;
		
		for(Object[]  objCE : lstED){
			objLearner = new LearnerRequest();
     		objLearner.setFirstName(objCE[1].toString());
     		objLearner.setLastName(objCE[2].toString());
     		objLearner.setEmail(objCE[3].toString());	
     		
     		List<CourseDetail> lst = new ArrayList<CourseDetail>();
			 List<Object[]> lstEnrollment = customerService.getEnrollmentsDetail(Long.valueOf(objCE[4].toString()));
			 for(Object[]  objEnrollment : lstEnrollment){
				 CourseDetail obj = new CourseDetail();
				 obj.setCourseName(objEnrollment[4].toString());
				 
				 if(objEnrollment[5].toString().equalsIgnoreCase("Scorm Course"))
					 obj.setCoursetype("Self Paced Course");
				 else
					 obj.setCoursetype(objEnrollment[5].toString());
				 
				 if(objEnrollment[6]!=null)
					 obj.setCourseStatus(objEnrollment[6].toString());
				 
				 lst.add(obj);
				 
			 }
			 objLearner.setEnrollmentsDetail(lst);
			 lstenrollmentDetail.add(objLearner);
		 }
		
        map.put("result", lstenrollmentDetail);
        map.put("status", Boolean.TRUE);
        map.put("message", "Success");
        return map;
        
	}
	
	
	
	
	@RequestMapping(value = "/enrollmentsByCustomerID", method = RequestMethod.GET)
	@ResponseBody
	public Map getEnrollmentsByCustomerID() throws Exception {
		
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		//String userName = auth.getName(); 
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		String userName = auth.getName(); //get logged in username
		
		Map<String, Object> col = new HashMap<String, Object>();
		//String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.findByUsername(userName); 
        List<EnrollmentDetailVO> lst = learnerService.getEnrollmentsByCustomerID(customer.getId());
		
        col.put("status", Boolean.TRUE);
        col.put("result", lst);

	     
		
	return col;
}
}
