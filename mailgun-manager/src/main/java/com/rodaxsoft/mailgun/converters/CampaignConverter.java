/**
	CampaignConverter.java
	
	Created by John Boyer on Aug 18, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun.converters;

import static org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.TransformedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import com.rodaxsoft.mailgun.Campaign;

/**
 * Campaign converter. 
 * Converts a {@link JSONObject} into a {@link Campaign} instance.
 * @author John Boyer
 * @version 2015-08-18
 */
public final class CampaignConverter implements Converter {

	private static final class KeyTransformer implements
			Transformer<String, String> {
		@Override
		public String transform(String input) {
			String result = input;
			
			int index = StringUtils.indexOf(input, '_');
			if(index != INDEX_NOT_FOUND) {
				StringBuilder buf = new StringBuilder(input);
				//Make uppercase and replace
				char upper = Character.toUpperCase(buf.charAt(index+1));
				buf.setCharAt(index+1, upper);
				//Remove underscores
				result = StringUtils.remove(buf.toString(), '_');
			}
			
			return result;
		}
	}

	@Override
	public <T> T convert(Class<T> type, Object value) {
		
		Campaign campaign = null;
		
		if(value instanceof JSONObject) {
			
			JSONObject json = (JSONObject) value;
			
			Transformer<String, String> keyTransformer;
			keyTransformer = new KeyTransformer();
			
			@SuppressWarnings("unchecked")
			final Map<String, Object> copy = new HashMap<>(json);
			Map<String, Object> transformed;
			transformed = TransformedMap.transformedMap(copy, keyTransformer, null);
			
			campaign = new Campaign();
			try {
				BeanUtils.populate(campaign, transformed);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new ContextedRuntimeException(e);
			}			
		}
	
		return type.cast(campaign);
	}

}
