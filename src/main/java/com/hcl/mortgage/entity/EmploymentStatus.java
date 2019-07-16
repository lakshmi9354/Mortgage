package com.hcl.mortgage.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmploymentStatus {
EMPLOYED,SELFEMPLOYED;
	
	private static Map<String, EmploymentStatus> rolesMap=new HashMap<>();
	static {
		rolesMap.put("employed",EMPLOYED);
		rolesMap.put("self employed",SELFEMPLOYED);		
	}
	
	@JsonCreator
    public static EmploymentStatus forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
