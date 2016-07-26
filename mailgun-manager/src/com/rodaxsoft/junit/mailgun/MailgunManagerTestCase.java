/**
	MailgunManagerTestCase.java
	
	Created by John Boyer on Oct 9, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.junit.mailgun;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.beanutils.ConfigurationDynaBean;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.lang3.exception.ContextedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rodaxsoft.mailgun.Campaign;
import com.rodaxsoft.mailgun.EmailRequest;
import com.rodaxsoft.mailgun.EmailValidationResponse;
import com.rodaxsoft.mailgun.ListInfo;
import com.rodaxsoft.mailgun.ListMember;
import com.rodaxsoft.mailgun.ListMemberRequest;
import com.rodaxsoft.mailgun.MailgunAccount;
import com.rodaxsoft.mailgun.MailgunManager;

/**
 * MailgunManagerTestCase class
 * 
 * @author John Boyer
 * @version 2016-07-23
 * @since 0.1
 */
public class MailgunManagerTestCase {
	/**
	 * List member email address
	 */
	private static final String LIST_MEMBER_ADDRESS = "john@example.com";
	/**
	 * Logging object
	 */
	public static final Log LOG = LogFactory
			.getLog(MailgunManagerTestCase.class);
	/**
	 * Campaign ID
	 */
	private static String sCampaignId;
	/**
	 * From email address
	 */
	private static String sFrom;
	/**
	 * Mailing list address
	 */
	private static String sMailingListAddress;
	/**
	 * To email address
	 */
	private static String sTo;
	/**
	 * Test case properties file
	 */
	private static final String TESTCASE_PROPERTIES = "testcase.properties";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		if (null == sMailingListAddress) {
			
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
			builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
			Parameters params = new Parameters();
			builder.configure(params.properties().setFileName(TESTCASE_PROPERTIES));
			Configuration config = builder.getConfiguration();
			
			DynaBean bean = new ConfigurationDynaBean(config);
			MailgunAccount acct = new MailgunAccount(bean);
//			Register the MailgunAccount object
			MailgunManager.register(acct);

			sMailingListAddress = config.getString("mailing.list");
			sFrom = config.getString("email.from");
			sTo = config.getString("email.to");
			sCampaignId = config.getString("campaign.id");
			
			LOG.info(acct);

		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @return A ListMemberRequest object
	 */
	private ListMemberRequest getListMemberRequest() {
		// Create the vars object
		JSONObject objects = new JSONObject();
		objects.put("city", "Seattle");
		objects.put("created", DateFormat.getInstance().format(new Date()));
		objects.put("province", "WA");

		// Create the request object
		ListMemberRequest member = new ListMemberRequest()
				.setAddress(LIST_MEMBER_ADDRESS).setName("John Doe")
				.setJSONVar(objects.toString());
		return member;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		boolean success;
		try {
			success = MailgunManager.deleteMailingListMember(sMailingListAddress, LIST_MEMBER_ADDRESS);
			if(success) {
				LOG.debug("Removed list member: " + LIST_MEMBER_ADDRESS);
			}
			else {
				LOG.warn("Unable to remove list member: " + LIST_MEMBER_ADDRESS);
			}
		} catch (ContextedException e) {
			LOG.error("Delete List Member Error", e);
		}
	}

	
	/**
	 * Test method for
	 * {@link MailgunManager#addMailingListMember(String, ListMemberRequest)}.
	 */
	@Test
	public void testAddMailingListMember() {

		ListMemberRequest member = getListMemberRequest();

		boolean success;
		try {
			success = MailgunManager.addMailingListMember(sMailingListAddress,
					member);
			assertTrue(success);

			success = MailgunManager.addMailingListMember(sMailingListAddress,
					member);
			assertFalse(success);

		} catch (Exception e) {
			LOG.error("Add List Member Error", e);
			fail(e.getMessage());
		}

	}
	
	/**
	 * Unimplemented
	 */
	@Test
	public void testAddMailingListMembers() {
		fail("Test method unimplmented");
	}
	
	/**
	 * Tests {@link MailgunManager#deleteMailingListMember(String, String)}
	 */
	@Test
	public void testDeleteMailingListMember() {
		
		boolean success;
		try {
			//Add the list member first
			success = MailgunManager.addMailingListMember(sMailingListAddress, getListMemberRequest());
			
			success = MailgunManager.deleteMailingListMember(sMailingListAddress, LIST_MEMBER_ADDRESS);
			assertTrue(success);
			
		} catch (ContextedException e) {
			LOG.error("Delete List Member Error", e);
			fail(e.getMessage());
		}

	}
	/**
	 * Tests {@link MailgunManager#getCampaign(String)}
	 */
	@Test
	public void testGetCampaign() {
		try {
			Campaign campaign = MailgunManager.getCampaign(sCampaignId);
			assertNotNull(campaign);
		} catch (ContextedException e) {
			LOG.error("Get Campaign Error", e);
			fail(e.getMessage());
		}
	}
	
	/**
	 * Tests {@link MailgunManager#getCampaignEvents(String)}
	 */
	@Test
	public void testGetCampaignEvents() {
		try {
			List<Map<String, Object>> events;
			events = MailgunManager.getCampaignEvents(sCampaignId);
			
			assertNotNull(events);
		} catch (ContextedException e) {
			LOG.error("Campaign Events Error", e);
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test method for {@link MailgunManager#getMailingListMembers(String)}
	 */
	@Test
	public void testGetMailingListMembersWithoutVarsType() {
		Object result;
		try {
			result = MailgunManager.getMailingListMembers(sMailingListAddress);
			assertTrue(result != null);

		} catch (Exception e) {
			LOG.error("Get Mailing List Members Error", e);
			fail(e.getMessage());
		}

	}

	/**
	 * Test for {@link MailgunManager#getMailingListMembers(String, Class)}
	 */
	@Test
	public void testGetMailingListMembersWithVarsType() {

		// Register converters
		Class<MemberDetails> varsType = MemberDetails.class;
		ConvertUtils.register(new MemberDetailsConverter(), varsType);

		Object result;
		try {
			result = MailgunManager.getMailingListMembers(sMailingListAddress,
					varsType);
			assertTrue(result != null);

		} catch (Exception e) {
			LOG.error("Get Mailing List Members Error", e);
			fail(e.getMessage());
		}

		finally {
			// De-register converters
			ConvertUtils.deregister();
		}

	}

	/**
	 * Tests {@link MailgunManager#getMailingListMember(String, String)}
	 */
	@Test
	public void testGetMailingListMemberWithoutVarsType() {
		
		try {
			
			ListMemberRequest request = getListMemberRequest();
			boolean success = MailgunManager.addMailingListMember(sMailingListAddress, request);
			assertTrue(success);
			
			ListMember member = MailgunManager.getMailingListMember(sMailingListAddress, LIST_MEMBER_ADDRESS);
			assertNotNull(member);
			
		} catch (ContextedException e) {
			LOG.error("Get Mailing List Member Error", e);
			fail(e.getMessage());
		}
		
	}
	/**
	 * Tests {@link MailgunManager#getMailingLists()}
	 */
	@Test
	public void testGetMailingLists() {
		
		try {
			//Fetch mailing lists
			Collection<ListInfo> lists = MailgunManager.getMailingLists();
			assertNotNull(lists);
			
		} catch (ContextedException e) {
			LOG.error("Mailing Lists Fetch Error", e);
			fail(e.getMessage());
		}
		
	}

	/**
	 * Test method for
	 * {@link com.rodaxsoft.mailgun.MailgunManager#isValidEmail(java.lang.String)}
	 * .
	 */
	@Test
	public void testIsValidEmail() {
		EmailValidationResponse response;
		try {
			response = MailgunManager.isValidEmail("john_e_boyer@yahoo.com");

			assertTrue(response.isValid());

			response = MailgunManager.isValidEmail("john_e_boyer@yah0o.com");
			assertTrue(response.isValid());
			assertTrue("null == didYouMean", response.getDidYouMean() != null);

		} catch (Exception e) {
			LOG.error("Email Validation Error", e);
			fail(e.getMessage());

		}
	}

	/**
	 * Tests {@link MailgunManager#saveCampaignEventsToCSV(String)}
	 */
	@Test
	public void testSaveCampaignEventsToCSV() {
		try {
			MailgunManager.saveCampaignEventsToCSV(sCampaignId);
		} catch (ContextedException | IOException e) {
			LOG.error("Save Campaign Event to CSV Error", e);
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.rodaxsoft.mailgun.MailgunManager#sendMessage(com.rodaxsoft.mailgun.EmailRequest)}
	 * 
	 */
	@Test
	public void testSendMessage() {
		EmailRequest emailRequest = new EmailRequest()
				.setTo(sTo)
				.setFrom(sFrom)
				.setSubject("Hello From " + getClass().getName())
				.setTextBody(
						"Hello:\n\nThis message was sent from my JUnit test case.")
				.setHeader("X-Test-Class", getClass().getSimpleName());
		
		boolean success;
		try {
			success = MailgunManager.sendMessage(emailRequest);
			assertTrue("Send mail failed", success);
			
		} catch (Exception e) {
			LOG.error("Email Validation Error", e);
			fail(e.getMessage());
		}

	}
	
	/**
	 * Tests {@link MailgunManager#unsubscribeMailingListMember(String, String)}
	 */
	@Test
	public void testUnsubscribeMailingListMember() {
		
		boolean success;
		try {
			//Add the list member first
			success = MailgunManager.addMailingListMember(sMailingListAddress, getListMemberRequest());
			
			success = MailgunManager.unsubscribeMailingListMember(sMailingListAddress, LIST_MEMBER_ADDRESS);
			assertTrue(success);
			
		} catch (ContextedException e) {
			LOG.error("Unsubscribe List Member Error", e);
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Tests {@link MailgunManager#updateMailingListMember(String, ListMemberRequest)}
	 */
	@Test
	public void testUpdateMailingListMember() {
		try {

			ListMemberRequest request = getListMemberRequest();
			boolean success = MailgunManager.addMailingListMember(sMailingListAddress, request);
			assertTrue(success);
			
			//Change the name
			request.setName("John Updated");
			
			success = MailgunManager.updateMailingListMember(sMailingListAddress, request);
			assertTrue(success);


		} catch (ContextedException e) {
			LOG.error("Get Mailing List Member Error", e);
			fail(e.getMessage());
		}
		
	}
}
