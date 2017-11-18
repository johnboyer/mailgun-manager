/**
	MailSender.java
	
	Created by John Boyer on Oct 7, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import org.apache.commons.lang3.exception.ContextedException;

import com.rodaxsoft.http.RESTResponse;

/**
 * MailSender concrete routine class
 * @author John Boyer
 * @version 2015-08-14
 * @since 0.1
 *
 */
final class MailSender extends AbstractMailgunRoutine {

	/**
	 * Constructor
	 * @param properties Mailgun account properties
	 */
	protected MailSender(MailgunAccount properties) {
		super(properties);

	}
	
	/**
	 * Sends an email with the given email request object
	 * @param emailRequest The email request object
	 * @return A <code>true</code> boolean value if successful; otherwise, <code>false</code>.
	 * @throws ContextedException
	 */
	boolean sendMessage(EmailRequest emailRequest) throws ContextedException {
		final String path = "/" + getAccount().getDomain() + "/messages";
		RESTResponse restResponse = invokePost(path, emailRequest.getParameters());
		return restResponse.getFamily() == SUCCESSFUL;
	}
	
}
