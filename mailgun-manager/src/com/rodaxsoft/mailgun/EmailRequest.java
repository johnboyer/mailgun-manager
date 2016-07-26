/**
	EmailRequest.java
	
	Created by John Boyer on Oct 8, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;


/**
 * EmailRequest class stores the message parameters
 * @author John Boyer
 * @version 2015-07-31
 * @since 0.1
 */
public class EmailRequest extends AbstractMailgunRequest {
	
	/**
	 * bcc key
	 */
	private static final String BCC_KEY = "bcc";
	/**
	 * Campaign key
	 */
	private static final String CAMPAIGN_KEY = "o:campaign";
	/**
	 * cc key
	 */
	private static final String CC_KEY = "cc";
	/**
	 * Header prefix
	 */
	private static final String HEADER_PREFIX = "h:";
	/**
	 * HTML key
	 */
	private static final String HTML_KEY = "html";
	/**
	 * Reply to key
	 */
	private static final String REPLY_TO_KEY = "h:Reply-To";
	/**
	 * Test mode key
	 */
	private static final String TESTMODE_KEY = "o:testmode";
	/**
	 * Text key
	 */
	private static final String TEXT_KEY = "text";
	/**
	 * To key
	 */
	private static final String TO_KEY = "to";
	/**
	 * Var prefix
	 */
	private static final String VAR_PREFIX = "v:";
	
	
	/**
	 * Returns the email address with the full name
	 * @param first First name
	 * @param last Last name
	 * @param email Email address
	 * @return  the email address with the full name
	 * @since 0.2
	 */
	public static String getAddressWithFullName(String first, String last, String email) {
		return new StringBuilder(first)
		              .append(' ')
		              .append(last)
		              .append(' ')
		              .append('<')
		              .append(email)
		              .append('>').toString();
	}
	
	/*
	 * @param name
	 * @return
	 */
	private String buildParameterName(String name, String parameterPrefix) {
		String paramName;
		final StringBuilder paramNameBuilder = new StringBuilder(parameterPrefix);
		
		if(!name.startsWith(parameterPrefix)) {
			paramName = paramNameBuilder.append(name).toString();
		}
		else {
			paramName = name;
		}
		return paramName;
	}
	
	private String buildRecipientsList(String... recipient) {
		StringBuilder recipients = new StringBuilder();
		final int size = recipient.length;
		for (int i = 0; i < size; i++) {
			recipients.append(recipient[i]);
			
			if(i < (size -1)) {
				recipients.append(',');
			}
		}
		return recipients.toString();
	}
	
	/**
	 * Sets a list of <i>bcc</i> recipients for the email. Example: 
	 * <code>Bob &lt;bob@host.com&gt;, Mary &lt;mary@host.com&gt;...</code>
	 * @param bcc The list of recipients
	 * @return EmailRequest object
	 */
	public EmailRequest setBCC(String...bcc) {
		String recipients = buildRecipientsList(bcc);
		parameters.put(BCC_KEY, recipients);
		return this;
	}
	
	/**
	 * Sets the campaign identifier.
	 * @param campaign The campaign identifier to set
	 * @return EmailRequest object
	 */
	public EmailRequest setCampaign(String campaign) {
		parameters.put(CAMPAIGN_KEY, campaign);
		return this;
	}
	
	/**
	 * Sets a list of <i>cc</i> recipients for the email. Example: 
	 * <code>Bob &lt;bob@host.com&gt;, Mary &lt;mary@host.com&gt;...</code>
	 * @param cc The list of recipients
	 * @return EmailRequest object
	 */
	public EmailRequest setCC(String...cc) {
		String recipients = buildRecipientsList(cc);
		parameters.put(CC_KEY, recipients);
		return this;
	}
	
	/**
	 * Sets the <i>from</i> address
	 * @param first First name
	 * @param last Last Name
	 * @param email Email address
	 * @return
	 * @since 0.2
	 */
	public EmailRequest setFrom(String first, String last, String email) {
		String from = getAddressWithFullName(first, last, email);
		return setFrom(from);
	}
	
	/**
	 * Sets the from email address.
	 * @param from The from email address
	 * @return EmailRequest object
	 */
	public EmailRequest setFrom(String from) {
		parameters.put("from", from);
		return this;
	}
	
	/**
	 * Sets a header with the name and value pair. For example:
	 * <code>X-My-Header-Name</code> and <code>My-Value</code>.
	 * @param name The header name
	 * @param value The header value
	 * @return EmailRequest object
	 */
	public EmailRequest setHeader(String name, String value) {		
		String headerName = buildParameterName(name, HEADER_PREFIX);	
		parameters.put(headerName, value);
		return this;
	}
	
	/**
	 * Sets the HTML body.
	 * @param html The HTML to set
	 * @return EmailRequest object
	 */
	public EmailRequest setHTMLBody(String html) {
		parameters.put(HTML_KEY, html);
		return this;
	}

	/**
	 * Sets custom data with the name and JSON value. For example:
	 * <code>my-custom-data</code> and <code>"{"my_message_id": 123}"</code>.
	 * @param name The var name
	 * @param value The JSON string value
	 * @return EmailRequest object
	 */	
	public EmailRequest setJSONVar(String name, String json) {
		String varName = buildParameterName(name, VAR_PREFIX);
		parameters.put(varName, json);
		return this;
	}
	
	/**
	 * Sets the reply-to email address.
	 * @param replyTo The reply-to email address
	 * @return EmailRequest object
	 */
	public EmailRequest setReplyTo(String replyTo) {
		parameters.put(REPLY_TO_KEY, replyTo);
		return this;
	}
	
	/**
	 * Sets the email subject.
	 * @param subject The subject to set
	 * @return EmailRequest object
	 */
	public EmailRequest setSubject(String subject) {
		parameters.put("subject", subject);
		return this;
	}

	/**
	 * Sets the test mode value.
	 * @param testMode The test mode value
	 * @return EmailRequest object
	 */
	public EmailRequest setTestMode(Boolean testMode) {
		parameters.put(TESTMODE_KEY, testMode.toString());
		return this;
	}
	
	/**
	 * Sets the plain text body.
	 * @param text The plain text to set
	 * @return EmailRequest object
	 */
	public EmailRequest setTextBody(String text) {
		parameters.put(TEXT_KEY, text);
		return this;
	}
	
	/**
	 * Sets the <i>to</i> address
	 * @param first First name
	 * @param last Last Name
	 * @param email Email address
	 * @return
	 * @since 0.2
	 */
	public EmailRequest setTo(String first, String last, String email) {
		String to = getAddressWithFullName(first, last, email);
		return setTo(to);
	}
	
	/**
	 * Sets a list of <i>to</i> recipients for the email. Example: 
	 * <code>Bob &lt;bob@host.com&gt;, Mary &lt;mary@host.com&gt;...</code>
	 * @param to The list of recipients
	 * @return EmailRequest object
	 */
	public EmailRequest setTo(String...to) {
		String recipients = buildRecipientsList(to);
		parameters.put(TO_KEY, recipients);
		return this;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailRequest [parameters=" + parameters + "]";
	}
}
