/**
	ListInfoConverter.java
	
	Created by John Boyer on Aug 12, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun.converters;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.Converter;

import com.rodaxsoft.mailgun.ListInfo;

/**
 * List member converter. 
 * Converts a {@link JSONObject} into a {@link ListInfo} instance.
 * @author John Boyer
 * @version 2015-08-18
 */
public final class ListInfoConverter implements Converter {

	/* (non-Javadoc)
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T convert(Class<T> type, Object value) {
		ListInfo info = null;
		
		if(value instanceof JSONObject) {
			JSONObject json = (JSONObject) value;
			info = new ListInfo();
			
			info.setAccessLevel(json.getString("access_level"));
			info.setAddress(json.getString("address"));
			info.setCount(json.getInt("members_count"));
			info.setCreated(json.getString("created_at"));
			info.setDescription(json.getString("description"));
			info.setName(json.getString("name"));
			
		}
		
		return type.cast(info);
	}

}
