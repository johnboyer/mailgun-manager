/**
	AbstractMailgunRoutine.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ContextedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rodaxsoft.http.RESTConnection;
import com.rodaxsoft.http.RESTConnectionFactory;
import com.rodaxsoft.http.RESTResponse;

/**
 * AbstractMailgunRoutine class
 * @author John Boyer
 * @version 2015-08-14
 * @since 0.1
 */
abstract class AbstractMailgunRoutine {

	/**
	 * Logging object
	 */
	protected static Log sLog;
	/**
	 * Mailgun account
	 */
	private final MailgunAccount account;

	/**
	 * Constructor
	 * @param account Mailgun account
	 */
	protected AbstractMailgunRoutine(MailgunAccount account) {
		this.account = account;
		sLog = LogFactory.getLog(getClass());
	}
	
	/**
	 * @return The Mailgun account info object
	 */
	protected MailgunAccount getAccount() {
		return account;
	}
	
	/**
	 * Invokes a GET request and returns a response object. By default,
	 * this method uses the private API key.
	 * @param path The resource path
	 * @return A response object
	 * @throws ContextedException if a connection error occurs 
	 * @since 0.2
	 */
	protected RESTResponse invokeGet(String path) throws ContextedException {
		return restConnection(account.getPrivateApiKey())
				.addPath(path)
				.configureRequest(APPLICATION_JSON).invokeGet();
		
	}
	
	/**
	 * Invokes a DELETE request and returns a response object. By default,
	 * this method uses the private API key.
	 * @param path The resource path
	 * @return A response object
	 * @throws ContextedException if a connection error occurs 
	 * @since 0.2
	 */
	protected RESTResponse invokeDelete(String path) throws ContextedException {
		return restConnection(account.getPrivateApiKey())
				.addPath(path)
				.configureRequest(APPLICATION_JSON).invokeDelete();
	}
	
	/**
	 * Invokes a POST request and returns a response object. By default,
	 * this method uses the private API key.
	 * @param path The resource path
	 * @param formParams The form parameters
	 * @return A response object
	 * @throws ContextedException if a connection error occurs 
	 * @since 0.1
	 */
	protected RESTResponse invokePost(String path, Map<String, String> formParams) 
			throws ContextedException {
		
		RESTResponse restResponse = restConnection(account.getPrivateApiKey()).
				addPath(path).
				addFormParams(formParams).
				configureRequest(APPLICATION_JSON).invokePost();
		return restResponse;
	}
	
	/**
	 * Invokes a PUT request and returns a response object. By default,
	 * this method uses the private API key.
	 * @param path The resource path
	 * @param formParams The form parameters
	 * @return A response object
	 * @throws ContextedException if a connection error occurs 
	 * @since 0.2
	 */
	protected RESTResponse invokePut(String path, Map<String, String> formParams) 
			throws ContextedException {
		
		RESTResponse restResponse = restConnection(account.getPrivateApiKey()).
				addPath(path).
				addFormParams(formParams).
				configureRequest(APPLICATION_JSON).invokePut();
		return restResponse;
	}
	
	/**
	 * Returns a connected REST connection object
	 * @param apiKey The API key
	 * @return A connected REST connection object
	 * @throws ContextedException if connection error occurs
	 */
	protected RESTConnection restConnection(String apiKey) throws ContextedException {

		try {
			
			return RESTConnectionFactory.getRESTConnection()
					                    .connect(account.getBaseUri(), apiKey);
		} 
		
		catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
		   throw new ContextedException("Instantiation Error", e)
		              .addContextValue("baseUri", account.getBaseUri())
		              .addContextValue("apiKey", apiKey);

		}
	}
	
	/**
	 * Converts a response to a JSON object
	 * @param response The string response
	 * @deprecated Use {@link RESTResponse#toJSONObject()} instead
	 * @return A JSON object
	 */
	protected JSONObject toJSONObject(String response) {
		return JSONObject.fromObject(response);
		
	}

}