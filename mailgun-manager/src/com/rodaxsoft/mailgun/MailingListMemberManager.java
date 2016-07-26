/**
	MailingListMemberManager.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.exception.ContextedException;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import com.rodaxsoft.http.RESTResponse;
import com.rodaxsoft.mailgun.converters.ListMemberConverter;

/**
 * MailingListMemberManager concrete routine class
 * @author John Boyer
 * @version 2015-08-20
 * @since 0.1
 */
final class MailingListMemberManager extends AbstractMailgunRoutine {
	
	/**
	 * Splits list into one list/1000 members
	 * @param listMembers The member list to split
	 * @return A list of member lists with sizes <code><=1000</code>
	 */
	private static List<List<ListMember>> splitList(List<ListMember> listMembers) {
		
		List<List<ListMember>> splitLists;

		//Split lists, Mailgun only supports adding 1000 members per API call
		if(listMembers.size() > 1000) {
			splitLists = ListUtils.partition(listMembers, 1000);
		}

		else {
			splitLists = new ArrayList<List<ListMember>>();
			splitLists.add(listMembers);
		}
		
		return splitLists;
	}
	
	/**
	 * The mailing list address
	 */
	private String listAddress;
	/**
	 * The list member converter object
	 */
	private final ListMemberConverter listMemberConverter = new ListMemberConverter();

	protected MailingListMemberManager(MailgunAccount properties) {
		super(properties);
		
		//Register converters
		ConvertUtils.register(listMemberConverter, ListMember.class);	
	}
	
	/**
	 * Constructor
	 * @param listAddress The mailing list address
	 * @param properties The mailgun properties
	 * @since 0.1
	 */
	protected MailingListMemberManager(String listAddress, MailgunAccount properties) {
		this(properties);
		this.listAddress = listAddress;
		
	}

	/**
	 * Adds s list member
	 * @param member The new list member
	 * @return A value of <code>true</code> if successful; otherwise, <code>false</code>.
	 * @throws ContextedException if a connection error occurs
	 * @since 0.1
	 */
	boolean addListMember(ListMemberRequest member) throws ContextedException {
		member.setSubscribed(true);
	
		final String path = "/lists/" + listAddress + "/members";
		return invokePost(path, member.getParameters()).success();	
	}
	
	/**
	 * Adds the list of members to a mailing list. List's with sizes <code>> 1000</code>
	 * are split into one list /1000 members. Duplicates are ignored.
	 * @param members The list of members to add
	 * @return A value of <code>true</code> if successful; otherwise, <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	boolean addListMembers(List<ListMember> members) throws ContextedException {

		if(members.size() > 1000) {
			
			List<List<ListMember>> splitLists = splitList(members);
			Iterator<List<ListMember>> iter = splitLists.iterator();

			boolean success = false;

			while(iter.hasNext()) {
				success = addListMembers(iter.next());

				if(!success) {
					System.err.println("Failed to add members");
				}
			}

			return success;
		}

		else {

			JSONArray jsonArray = new JSONArray();
			for (ListMember m : members) {
				jsonArray.add(m.toMap());

			}

			final String path = "/lists/" + listAddress + "/members.json";
			Map<String, String> params = new HashMap<>();
			params.put("members", jsonArray.toString());
			return invokePost(path, params).success();
		}

	}
	
	/**
	 * Removes the list member with the given email address
	 * @param email The email address of the list member
	 * @return A value of <code>true</code> if successful; otherwise, 
	 *         <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 */
	boolean deleteListMember(String email) throws ContextedException {
		final String path = "/lists/" + listAddress + "/members/" + email;
		return invokeDelete(path).success();
	}
	
	/**
	 * @return the listAddress
	 * @since 0.1
	 */
	String getListAddress() {
		return listAddress;
	}
	
	ListMember getListMember(String email) throws ContextedException {
		final String path = "/lists/" + listAddress + "/members/" + email;
		RESTResponse response = invokeGet(path);
		
		ListMember member = null;
		if(response.success()) {
			JSONObject obj = response.toJSONObject().getJSONObject("member");
		
			member = (ListMember) ConvertUtils.convert(obj, ListMember.class);
		}
		
		return member;
		
	}
	
	/**
	 * Returns the list members
	 * @return the list members
	 * @throws ContextedException If no registered {@link Converter} is found 
	 *         for the varsType
	 * @since 0.2
	 */
	List<ListMember> getListMembers() throws ContextedException {
		return getListMembers(null);
	}
	
	
	/**
	 * Converts the <code>vars</code> object into an instance of the varsType and 
     * returns the members of the mailing list
	 * @param varsType The target type for the <i>vars</i> object. 
	 *        <i>Note: If no registered {@link Converter} is found for the varsType, 
	 *        a {@link ContextedRuntimeException} will be thrown.</i>
	 * @return A list of list member objects
	 * @throws ContextedException If no registered {@link Converter} is found for the varsType
	 * @see ConvertUtils#register(Converter, Class)
	 * @since 0.2
	 * 
	 */
	List<ListMember> getListMembers(Class<?> varsType) throws ContextedException {
		
       List<ListMember> members = null;
       
		if (varsType != null) {
			
			if(ConvertUtils.lookup(varsType) != null) {
				listMemberConverter.setVarClass(varsType);			
			}
			
			else {
				ContextedException cre;
				cre = new ContextedException("No registered Converter for varType");
				cre.addContextValue("varType", varsType.getName());
				throw cre;
			}
		}
		
		final String path = "/lists/" + listAddress + "/members";
		RESTResponse response = invokeGet(path);
		
		if(response.success()) {
			JSONObject jsonObj = response.toJSONObject();
			JSONArray jsonArray = jsonObj.getJSONArray("items");
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iter = jsonArray.iterator();
			
			while(iter.hasNext()) {
				
				JSONObject obj = iter.next();
				
				if(null == members) {
					members = new ArrayList<>();
				}
				
				ListMember lm = (ListMember) ConvertUtils.convert(obj, ListMember.class);
				members.add(lm);
			}

		}
		
		return members;
		
	}
	
	/**
	 * @param listAddress the listAddress to set
	 * @since 0.1
	 */
	void setListAddress(String listAddress) {
		this.listAddress = listAddress;
	}
	
	/**
	 * Unsubscribes the list member with given email address
	 * @param email The email address of the list member
	 * @return A value of <code>true</code> if successful; otherwise, 
	 *         <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 */
	boolean unsubscribeListMember(String email) 
			throws ContextedException {
		
		final String path = "/lists/" + listAddress + "/members/" + email;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("subscribed", "no");
		return invokePut(path, params).success();
	}
	
	/**
	 * Updates the list member 
	 * @param request The list member request
	 * @return A value of <code>true</code> if successful; otherwise, 
	 *         <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 */
	boolean updateListMember(ListMemberRequest request) throws ContextedException {
		final Map<String, String> formParams = request.getParameters();
		final String email = formParams.get(ListMemberRequest.ADDRESS_KEY);
		
		final String path = "/lists/" + listAddress + "/members/" + email;
		return invokePut(path, formParams).success();		
	}

}
