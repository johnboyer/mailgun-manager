/**
	MailingListManager.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.exception.ContextedException;

import com.rodaxsoft.http.RESTResponse;
import com.rodaxsoft.mailgun.converters.ListInfoConverter;

/**
 * MailingListManager concrete routine class
 * @author John Boyer
 * @version 2015-08-18
 * @since 0.1
 */
final class MailingListManager extends AbstractMailgunRoutine {
	
	/**
	 * Constructor
	 * @param properties Mailgun account info
	 */
	protected MailingListManager(MailgunAccount properties) {
		super(properties);
		
		ConvertUtils.register(new ListInfoConverter(), ListInfo.class);
	}
	
	/**
	 * Returns the mailing lists for the account
	 * @return A collection of list info objects 
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	Collection<ListInfo> getLists() throws ContextedException {
		final String path = "/lists";
		RESTResponse restResponse = invokeGet(path);
		
		List<ListInfo> lists = null;
		
		if(restResponse.getFamily() == SUCCESSFUL) {

			JSONObject jsonObj = toJSONObject(restResponse.getStringResponse());
			JSONArray jsonArray = jsonObj.getJSONArray("items");
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iter = jsonArray.iterator();
			
			while(iter.hasNext()) {
				
				JSONObject anItem = iter.next();
				
				if(null == lists) {
					lists = new ArrayList<>();
				}
				
				ListInfo info;
				info = (ListInfo) ConvertUtils.convert(anItem, ListInfo.class);
				lists.add(info);
				
			}
			
		}
		
		return lists;
	}

}
