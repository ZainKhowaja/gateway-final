package com.softech.ls360.api.gateway.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.softech.ls360.api.gateway.service.VILTAttendanceService;
import com.softech.ls360.lms.repository.entities.VILTAttendance;
import com.softech.ls360.lms.repository.repositories.VILTAttendanceRepository;

@Service
public class VILTAttendanceServiceImpl implements VILTAttendanceService {

	@Inject
	private VILTAttendanceRepository viltAttendanceRepository;
	
	public void addVILTAttendance(VILTAttendance viltAttendance){
		//viltAttendanceRepository.sa
	}
}
