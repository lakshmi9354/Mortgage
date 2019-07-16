package com.hcl.mortgage.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Title {
	MR,MRS;
	
	private static Map<String, Title> rolesMap=new HashMap<>();
	static {
		rolesMap.put("Mr",MR);
		rolesMap.put("Mrs",MRS);		
	}
	
	@JsonCreator
    public static Title forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
