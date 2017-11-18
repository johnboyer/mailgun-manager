/**
	EmailValidator.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import static com.rodaxsoft.mailgun.EmailValidationResponse.DID_YOU_MEAN_KEY;
import static com.rodaxsoft.mailgun.EmailValidationResponse.IS_VALID_KEY;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.validator.routines.EmailValidator.getInstance;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ContextedException;

import com.rodaxsoft.http.RESTConnection;
import com.rodaxsoft.http.RESTResponse;

/**
 * EmailValidator class is facade of the Mailgun EmailRequest Validation service 
 * @author John Boyer
 * @version 2017-11-18
 * @since 0.1
 *
 */
final class EmailValidator extends AbstractMailgunRoutine {

	/**
	 * Constructs the email validator object
	 */
	EmailValidator(MailgunAccount properties)  {
		super(properties);
	}
		
	/**
	 * Checks the validity of the email address using the 
	 * Mailgun EmailRequest Validation API
	 * @param emailAddress
	 * @return An {@link EmailValidationResponse} object
	 * @throws ContextedException if a connection error occurs
	 */
	EmailValidationResponse isValid(String emailAddress) throws ContextedException {
		final org.apache.commons.validator.routines.EmailValidator commonsValidator = getInstance();

		EmailValidationResponse emailValidationResponse = new EmailValidationResponse(emailAddress, false, null);

		final boolean isCommonsValid = commonsValidator.isValid(emailAddress);
		sLog.debug("Apache Commons EmailValidator Response: " + isCommonsValid);
		if(!isCommonsValid) {
			return emailValidationResponse;
		}
		else {

			RESTConnection conn = restConnection(getAccount().getPublicApiKey());
			RESTResponse restResponse = conn.addPath("/address/validate").
					addQueryParam("address", emailAddress).
					configureRequest(APPLICATION_JSON).
					invokeGet();

			JSONObject jsonOutput = restResponse.toJSONObject();
			Object object = jsonOutput.get(DID_YOU_MEAN_KEY);
			String didYouMean;
			if(object instanceof JSONNull) {
				didYouMean = null;
			} else {
				didYouMean = jsonOutput.getString(DID_YOU_MEAN_KEY);
			}
			
			emailValidationResponse = 
					new EmailValidationResponse(emailAddress, jsonOutput.getBoolean(IS_VALID_KEY), didYouMean);

			return emailValidationResponse;	
		}
	}
}
