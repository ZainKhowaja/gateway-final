package com.softech.ls360.lms.repository.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VILT_Attendance")
public class VILTAttendance extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -8147590863224265540L;
	private LearnerEnrollment learnerEnrollment;
	private Date attendanceDate;
	
	@OneToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="LEARNERENROLLMENT_ID")
	public LearnerEnrollment getLearnerEnrollment() {
		return learnerEnrollment;
	}
	
	public void setLearnerEnrollment(LearnerEnrollment learnerEnrollment) {
		this.learnerEnrollment = learnerEnrollment;
	}
	
	@Column(name = "ATTENDANCE_DATE")
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	
	
}
