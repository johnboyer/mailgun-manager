/**
	MailgunManager.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.beanutils.ConfigurationDynaBean;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.exception.ContextedException;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MailgunManager class
 * @author John Boyer
 * @version 2016-07-23
 * @since 0.1
 */
public final class MailgunManager {
	
	/**
	 * Logging object
	 */
	private static final Log LOG = LogFactory.getLog(MailgunManager.class);
	/**
	 * Mailgun properties file, place in <code>CLASSPATH</code>
	 */
	public static final String MAILGUN_PROPERTIES = "mailgun.properties";
	/**
	 * Mailgun account info
	 */
	private static MailgunAccount sMailgunAccount;
	
	/**
	 * Adds a member to a mailing list
	 * @param listAddress The mailing list 
	 * @param member The new list member
	 * @return A value of <code>true</code> if the member is added; otherwise false.
	 * @throws ContextedException if connection error occurs
	 * @since 0.1
	 */
	public static boolean addMailingListMember(String listAddress, ListMemberRequest member) 
			throws ContextedException {
		
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.addListMember(member);
	}

	/**
	 * Adds the list members to the mailing list
	 * @param listAddress The mailing list 
	 * @param members The list of members
	 * @return A value of <code>true</code> if the list members are added; otherwise <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	public static boolean addMailingListMembers(String listAddress, List<ListMember> members) 
			throws ContextedException {
		
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.addListMembers(members);
	}
	
	/**
	 * Deletes a member from a mailing list
	 * @param listAddress The mailing list 
	 * @param emailAddress The member's email address
	 * @return A value of <code>true</code> if the member is removed; otherwise <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	public static boolean deleteMailingListMember(String listAddress, String emailAddress) 
			throws ContextedException {
	    
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.deleteListMember(emailAddress);	
	}
	
	/**
	 * Returns the campaign for the given ID
	 * @param campaignId The campaign ID
	 * @return the campaign for the given ID or <code>null</code>
	 * @throws ContextedException if a processing error occurs
	 */
	public static Campaign getCampaign(String campaignId) throws ContextedException {
		validateRegistration();
		
		return new CampaignManager(sMailgunAccount).getCampaign(campaignId);
	}
	
	/**
	 * Returns campaign events as list of maps
	 * @param campaignId The campaign ID for the events
	 * @return A list of campaign events as map objects or <code>null</code>
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	public static List<Map<String, Object>> getCampaignEvents(String campaignId) 
			                                        throws ContextedException {
		validateRegistration();
		
		return new CampaignManager(sMailgunAccount).getEvents(campaignId);
	}
	
	/**
	 * Returns the member of the mailing list
	 * @param listAddress The mailing list
	 * @return The member of the mailing list
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
    public static ListMember getMailingListMember(String listAddress, String email) 
    		throws ContextedException {
    	
		validateRegistration();
		
		MailingListMemberManager memberMgr = new MailingListMemberManager(listAddress, sMailgunAccount);
		return memberMgr.getListMember(email);
	}
	
	/**
	 * Returns the members of the mailing list
	 * @param listAddress The mailing list
	 * @return The members of the mailing list
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
    public static List<ListMember> getMailingListMembers(String listAddress) 
    		throws ContextedException {
    	
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.getListMembers();
	}
	
	/**
     * Converts the <code>vars</code> object into an instance of the varsType and 
     * returns the members of the mailing list
     * @param listAddress The mailing list
     * @param varsType The type of the <code>vars</code> object
     * @return The members of the mailing list
     * @throws ContextedException if a processing error occurs
     * @since 0.2
     */
	public static List<ListMember> getMailingListMembers(String listAddress, Class<?> varsType) 
			throws ContextedException {
		
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.getListMembers(varsType);
	}
	
	/**
	 * Returns the mailing lists for the account
	 * @return A collection of list info objects 
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	public static Collection<ListInfo> getMailingLists() throws ContextedException {
		
		validateRegistration();
		
		return new MailingListManager(sMailgunAccount).getLists();
	}
	
	private static boolean hasProperties() {
		final ClassLoader cl = MailgunManager.class.getClassLoader();
		return cl.getResource(MAILGUN_PROPERTIES) != null;
	}

    /**
	 * Validates the email address and returns a response object.
	 * @param email The email address to validate
	 * @return The email validation response object
	 * @throws ContextedException if a connection error occurs
	 * @since 0.1
	 */
	public static EmailValidationResponse isValidEmail(String email) 
			throws ContextedException {
		
		validateRegistration();
		
		EmailValidator emaiValidator = new EmailValidator(sMailgunAccount);
		return emaiValidator.isValid(email);
	}
	
	private static void loadProperties() {
		
		if(hasProperties()) {

			LOG.info("Loading mailgun properties...");

			FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

			final Class<PropertiesConfiguration> propClazz = PropertiesConfiguration.class;
			builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(propClazz);
			
			Parameters params = new Parameters();
			final File file = new File(MAILGUN_PROPERTIES);
			builder.configure(params.fileBased().setFile(file));
			
			PropertiesConfiguration config;
			try {
				config = builder.getConfiguration();
				DynaBean bean = new ConfigurationDynaBean(config);
				sMailgunAccount = new MailgunAccount(bean);
				
			} catch (ConfigurationException e) {
				throw new ContextedRuntimeException("Error loading mailgun.properties", e);
			}
			
		}

		else {
			LOG.warn("No `mailgun.properties` in CLASSPATH");
		}

	}
	
	/**
	 * Registers the Mailgun account info
	 * @param account The Mailgun account info to register
	 */
	public static void register(MailgunAccount account) {
		sMailgunAccount = account;		
	}

	/**
	 * Saves campaign events to a CSV file with the following format:
	 * <code>&lt;campaign name&gt;_(&lt;campaign id&gt;)_&lt;timestamp&gt;.csv</code>
	 * @param campaignId The campaign ID
	 * @throws ContextedException if a processing error occurs
	 * @throws IOException if an I/O error occurs
	 */
	public static void saveCampaignEventsToCSV(String campaignId) 
			throws ContextedException, IOException {
		validateRegistration();
		
		new CampaignManager(sMailgunAccount).saveCampaignEventsToCSV(campaignId);
	}
	
	/**
	 * Sends the email message.
	 * @param emailRequest The message properties to send
	 * @return A boolean value of <code>true</code> if the message is sent 
	 *         successfully; otherwise <code>false</code>.
	 * @throws ContextedException if a connection error occurs
	 * @since 0.1
	 */
	public static boolean sendMessage(EmailRequest emailRequest) 
			throws ContextedException {
		
		validateRegistration();
		
		MailSender sender = new MailSender(sMailgunAccount);
		return sender.sendMessage(emailRequest);
	}

	/**
	 * Unsubscribes the list member with given email address
	 * @param listAddress The mailing list
	 * @param emailAddress The email address of the list member
	 * @return A value of <code>true</code> if successful; otherwise, 
	 *         <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 */
	public static boolean unsubscribeMailingListMember(String listAddress, String emailAddress) 
			throws ContextedException {
		
		validateRegistration();
		
		MailingListMemberManager listManager = new MailingListMemberManager(listAddress, sMailgunAccount);
		return listManager.unsubscribeListMember(emailAddress);
	}
	
	/**
	 * Updates the list member represented by the request
	 * @param listAddress The mailing list
	 * @param request The list member request
	 * @return A value of <code>true</code> if successful; otherwise, 
	 *         <code>false</code>.
	 * @throws ContextedException if a processing error occurs
	 */
	public static boolean updateMailingListMember(String listAddress, ListMemberRequest request) 
			throws ContextedException {
		
		validateRegistration();
		
		return new MailingListMemberManager(listAddress, sMailgunAccount)
		                 .updateListMember(request);
		
	}
	/**
	 * Validates Mailgun account registration
	 */
	private static void validateRegistration() {
		
		if(null == sMailgunAccount) {
			
			LOG.debug("MailgunAccount is null, will try to load " + MAILGUN_PROPERTIES);
			loadProperties();

			if(null == sMailgunAccount) {
				
				final String msg = "No mailgun account settings. Call register(MailgunAccount) or put a `mailgun.properties` file in the CLASSPATH";
				throw new ContextedRuntimeException(msg)
				            .addContextValue("mailgunAccount", null);
			}
			
			else {
				LOG.info("Successfully loaded " + MAILGUN_PROPERTIES);
			}
		}
	}
	
	/**
	 * Constructor
	 */
	private MailgunManager() {
		
	}
	
}
