package com.hcl.mortgage.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Occupation {
	PROFESSIONAL,BUSSINESS;
	
	private static Map<String, Occupation> rolesMap=new HashMap<>();
	static {
		rolesMap.put("professional",PROFESSIONAL);
		rolesMap.put("business",BUSSINESS);		
	}
	
	@JsonCreator
    public static Occupation forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
