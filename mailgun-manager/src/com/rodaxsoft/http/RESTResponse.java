/**
	RESTResponse.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.http;

import javax.ws.rs.core.Response.Status.Family;

import net.sf.json.JSONObject;

/**
 * RESTResponse
 * @author John Boyer
 * @version 2015-08-20
 * @since 0.1
 * 
 */
public interface RESTResponse {
	/**
	 * @return Response family
	 */
	Family getFamily();
	/**
	 * @return String response
	 */
	String getStringResponse();
	
	/**
	 * Converts response into a JSON object
	 * @return  a JSON object
	 */
	JSONObject toJSONObject();
	/**
	 * @return A value of true if successful; otherwise, <code>false</code>.
	 */
	boolean success();
}
