package com.hcl.mortgage.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OperationType {
	BUYINGMYFIRSTHOME,
	MOVINGTOANOTHERHOME,
	BUYINGAPROPERYLET
	;

	private static Map<String, OperationType> rolesMap=new HashMap<>();
	static {
		rolesMap.put("buying my first home",BUYINGMYFIRSTHOME);
		rolesMap.put("moving to another home",MOVINGTOANOTHERHOME);
		rolesMap.put("buying a property",BUYINGAPROPERYLET);
		
	}
	
	@JsonCreator
    public static OperationType forValue(String value) {
		if(value==null)
			return null;
        return rolesMap.get(value.toLowerCase());
    }
}
