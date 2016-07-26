/**
	ListMemberConverter.java
	
	Created by John Boyer on Aug 12, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun.converters;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.rodaxsoft.mailgun.ListMember;

/**
 * List member converter. 
 * Converts a {@link JSONObject} into a {@link ListMember} instance.
 * @author John Boyer
 * @version 2015-08-20
 * @since 0.2
 */
public final class ListMemberConverter implements Converter {
	
	/**
	 * Var class type
	 */
	private Class<?> varClass;

	@Override
	public <T> T convert(Class<T> type, Object value) {

		ListMember lm = null;
		
		if(value instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) value;
			
			lm = new ListMember();
			lm.setAddress(jsonObj.getString("address"));
			lm.setName(jsonObj.getString("name"));
			lm.setSubscribed(jsonObj.getBoolean("subscribed"));
			
			JSONObject varsObj = jsonObj.optJSONObject("vars");
			
			if(varsObj != null) {			
				
				//Use a converter if varClass defined
				if(varClass != null &&  
						ConvertUtils.lookup(JSONObject.class, varClass) != null) {
					lm.setVars(ConvertUtils.convert(varsObj, varClass));
				}
				
				else {
					lm.setVars(varsObj);
				}
			}
			
		}
		
		return type.cast(lm);
	}

	/**
	 * @return the varClass
	 */
	public Class<?> getVarClass() {
		return varClass;
	}

	/**
	 * @param varClass the varClass to set
	 */
	public void setVarClass(Class<?> varClass) {
		this.varClass = varClass;
	}
	
}