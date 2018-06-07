package com.softech.ls360.api.gateway.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.softech.ls360.api.gateway.service.VILTAttendanceService;
import com.softech.ls360.lms.repository.entities.VILTAttendance;
import com.softech.ls360.lms.repository.repositories.LearnerCourseStatisticsRepository;
import com.softech.ls360.lms.repository.repositories.VILTAttendanceRepository;

@Service
public class VILTAttendanceServiceImpl implements VILTAttendanceService {

	@Inject
	private VILTAttendanceRepository viltAttendanceRepository;
	
	@Inject
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRepository;
	
	@Value( "${clasroomcourse.class.duration}" )
    private String classDuration;
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public void addVILTAttendance(HashMap<Long,List<String>> attendance){
	
		List<Long> deleteAttendance = new ArrayList<Long>();
		List<VILTAttendance> lstVILTAttendance = new ArrayList<VILTAttendance>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Map.Entry<Long,List<String>> entry : attendance.entrySet()) {
			deleteAttendance.add(entry.getKey());
			List<String> attendanceDate = entry.getValue();
			for(String date : attendanceDate){
				VILTAttendance viltAttendance = new VILTAttendance();
				viltAttendance.setEnrollmentId(entry.getKey());
				try {
					//Date date1 = formatter.parse(date);
					viltAttendance.setAttendanceDate(formatter.parse(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lstVILTAttendance.add(viltAttendance);
			}
	    }
		viltAttendanceRepository.deleteByEnrollmentIdIn(deleteAttendance);
		viltAttendanceRepository.save(lstVILTAttendance);
		//learnerCourseStatisticsRepository.markCompletion(deleteAttendance, dtf.format(LocalDateTime.now()));
		
		// add new loop here
		for (Map.Entry<Long,List<String>> entry : attendance.entrySet()) {
			Long enrollmentId = entry.getKey();
			List<String> attendanceDate = entry.getValue();
			Long totalTimeSpent = (long) (attendanceDate.size() * Long.valueOf(classDuration) * 60 * 60) ;
			learnerCourseStatisticsRepository.markCompletionAndTotalTimeSpent(enrollmentId, dtf.format(LocalDateTime.now()), totalTimeSpent);
		}
		
	}
	
	public List<Object[]> findByEnrollmentIds( Long ids){
		List<Object[]> lst = viltAttendanceRepository.findByEnrollmentIds(ids);
		return lst;
	}
}
