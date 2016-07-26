/**
	RESTConnection.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.http;

import java.util.Map;

/**
 * RESTConnection interface
 * @author John Boyer
 * @version 2015-08-04
 * @since 0.1
 */
public interface RESTConnection {
	
	/**
	 * Adds the form parameters
	 * @param params The form parameters to add
	 * @return The REST connection object
	 */
	RESTConnection addFormParams(Map<String, String> params);
	/**
	 * Append a path to the current URI
	 * @param path The path
	 * @return The REST connection object
	 */
	RESTConnection addPath(String path);
	/**
	 * Adds query parameter to the URI
	 * @param name The paramater's name
	 * @param value The paramater's value
	 * @return The REST connection object
	 */
	RESTConnection addQueryParam(String name, String value);
	/**
	 * Configures the request with media type
	 * @param mediaType The media type string value
	 * @return The REST connection object
	 */
	RESTConnection configureRequest(String mediaType);
	/**
	 * Establishes a REST connection and returns it
	 * @param baseUri The REST base URI
	 * @param apiKey The API key
	 * @return A REST connection 
	 */
	RESTConnection connect(String baseUri, String apiKey);
	/**
	 * Invokes a GET request and returns a response object
	 * @return The response
	 * @since 0.2
	 */
	RESTResponse invokeGet();
	/**
	 * Invokes a DELETE request and returns a response object
	 * @return The response
	 * @since 0.2
	 */
	RESTResponse invokeDelete();
	/**
	 * Invokes a POST request and returns a response object
	 * @return The response
	 */
	RESTResponse invokePost();
	/**
	 * Invokes a PUT request and returns a response object
	 * @return The response
	 * @since 0.2
	 */
	RESTResponse invokePut();
	
}
