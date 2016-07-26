/**
	ListMemberRequest.java
	
	Created by John Boyer on Oct 9, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * ListMemberRequest class stores parameters
 * @author John Boyer
 * @version 2015-07-31
 * @since 0.1
 */
public final class ListMemberRequest extends AbstractMailgunRequest {
	
	protected static final String VARS_KEY = "vars";
	protected static final String ADDRESS_KEY = "address";
	protected static final String NAME_KEY = "name";
	protected static final String SUBSCRIBED_KEY = "subscribed";

	/**
	 * Sets the subscribed boolean value
	 * @param subscribed The subscribed boolean value 
	 * @return ListMemberRequest object
	 */
	public ListMemberRequest setSubscribed(Boolean subscribed) {
		parameters.put(SUBSCRIBED_KEY, subscribed.toString());
		return this;
	}
	
	/**
	 * Sets the list member name
	 * @param name The name to set
	 * @return ListMemberRequest object
	 */
	public ListMemberRequest setName(String name) {
		parameters.put(NAME_KEY, name);
		return this;
	}
	
	/**
	 * Sets custom data with the JSON string. For example:
	 * <code>"{"my_message_id": 123}"</code>.
	 * @param json The JSON object
	 * @return ListMemberRequest object
	 */	
	public ListMemberRequest setJSONVar(JSONObject json) {
		return json != null ? setJSONVar(json.toString()): this;
	}
	
	/**
	 * Sets custom data with the JSON string. For example:
	 * <code>"{"my_message_id": 123}"</code>.
	 * @param json The JSON string value
	 * @return ListMemberRequest object
	 */	
	public ListMemberRequest setJSONVar(String json) {
		parameters.put(VARS_KEY, json);
		return this;
	}
	
	/**
	 * Set the list member email address
	 * @param address The email address to set
	 * @return ListMemberRequest object
	 * @throws ContextedRuntimeException if the address format is invalid.
	 * 
	 */
	public ListMemberRequest setAddress(String address) {
		try {
			new InternetAddress(address, true);
		} catch (AddressException e) {
			throw new ContextedRuntimeException(e)
			          .addContextValue("address", address)
			          .addContextValue("position", e.getPos());
		}
		
		parameters.put(ADDRESS_KEY, address);
		return this;
	}
	
	/**
	 * Converts a {@link ListMember} object into a {@link ListMemberRequest} instance
	 * @param member The list member to convert
	 * @return A list member request object
	 */
	public static ListMemberRequest valueOf(ListMember member) {
		
		JSONObject varsObj = null;
		if(member.getVars() != null) {
			varsObj = JSONObject.fromObject(member.getVars());
		}
		
		return new ListMemberRequest()
		             .setAddress(member.getAddress())
		             .setJSONVar(varsObj)
		             .setName(member.getName())
		             .setSubscribed(member.isSubscribed());
	}
	

	
}
