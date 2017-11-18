/**
	MemberDetailsConverter.java
	
	Created by John Boyer on Jul 27, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.junit.mailgun;

import java.lang.reflect.InvocationTargetException;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * Member traits converter class
 * @author John Boyer
 * @version 2015-07-27
 * @since 0.2
 */
public final class MemberDetailsConverter implements Converter {


	/* (non-Javadoc)
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T convert(Class<T> type, Object value) {
		
		MemberDetails details = null;
		
		if(value instanceof JSONObject) {
			
			JSONObject obj = (JSONObject) value;
			
			details = new MemberDetails();
			
			try {
				BeanUtils.populate(details, obj);
			} catch (IllegalAccessException | InvocationTargetException e) {
				ContextedRuntimeException cre;
				cre = new ContextedRuntimeException("Bean population error", e);
				cre.addContextValue("value", obj);
				throw cre;
			}
			
		}

		return type.cast(details);
	}

}
