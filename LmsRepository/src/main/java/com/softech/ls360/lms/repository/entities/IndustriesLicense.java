package com.softech.ls360.lms.repository.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LICENSE_INDUSTRIES")
public class IndustriesLicense extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String shortName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
