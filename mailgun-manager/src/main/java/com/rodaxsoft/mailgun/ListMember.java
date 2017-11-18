/*
	ListMember.java
	
	Created by John Boyer on Jul 27, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * List member class
 * @author John Boyer
 * @version 2015-08-15
 * @since 0.2
 */
public class ListMember extends Address implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -4627755540397201027L;
	/**
	 * Email address
	 */
	private String address;
	/**
	 * Full name
	 */
	private String name;
	/**
	 * Subscribed flag
	 */
	private boolean subscribed;
	/**
	 * Vars object 
	 */
	private Object vars;
	

	/**
	 * Default constructor
	 */
	public ListMember() {
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof ListMember)) {
			return false;
		}
		
		ListMember rhs = (ListMember) obj;
		return new EqualsBuilder()
		            .append(address.toLowerCase(), rhs.address.toLowerCase())
		            .isEquals();
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String getType() {

		if(address != null) {
			try {
				return new InternetAddress(address, name).getType();
			} catch (UnsupportedEncodingException e) {
				throw new ContextedRuntimeException(e);
			}
		}
		else {
			return null;
		}
	}

	/**
	 * @return the vars
	 */
	public Object getVars() {
		return vars;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 17).append(address).toHashCode();
	}

	/**
	 * @return the subscribed
	 */
	public boolean isSubscribed() {
		return subscribed;
	}


	/**
	 * Sets the email address
	 * @param address The email address to set
	 * @throws ContextedRuntimeException if the email address format is invalid
	 */
	public void setAddress(String address) {
		
		try {
			new InternetAddress(address, true);
		} catch (AddressException e) {
			throw new ContextedRuntimeException(e)
			          .addContextValue("address", address)
			          .addContextValue("position", e.getPos());
		}
		
		this.address = address;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param subscribed the subscribed to set
	 */
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	
	/**
	 * @param vars the vars to set
	 */
	public void setVars(Object vars) {
		this.vars = vars;
	}

	/**
	 * @return A list member request representation of the object
	 */
	public ListMemberRequest toListMemberRequest() {
		ListMemberRequest request; 
		request = new ListMemberRequest()
		                 .setAddress(address)
		                 .setName(name)
		                 .setSubscribed(subscribed);
		
		if(vars != null) {
			
			if(vars instanceof JSONObject) {
				request.setJSONVar((JSONObject) vars);
			}
			
			else {
				JSONObject jsonObj = JSONObject.fromObject(vars);
				request.setJSONVar(jsonObj);
			}
			
		}
		
		return request;
	}
	
	/**
	 * @return A map representation of the object
	 */
	public Map<Object, Object> toMap() {
		return new BeanMap(this);
	}

	/**
	 * Return a String representation of this address object.
	 */
	@Override
	public String toString() {
		if(address != null) {
			try {
				return new InternetAddress(address, name).toString();
			} catch (UnsupportedEncodingException e) {
				throw new ContextedRuntimeException(e);
			}
		}

		else {
			return null;
		}
	}
	
}
