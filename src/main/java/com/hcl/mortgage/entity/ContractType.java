package com.hcl.mortgage.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ContractType {
PROBATIONARY,TEMPORAY;
	
	private static Map<String, ContractType> rolesMap=new HashMap<>();
	static {
		rolesMap.put("probationary",PROBATIONARY);
		rolesMap.put("temporary",TEMPORAY);		
	}
	
	@JsonCreator
    public static ContractType forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
