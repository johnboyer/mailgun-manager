/**
	RESTConnectionFactory.java
	
	Created by John Boyer on Jul 31, 2015
	(c) Copyright 201 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.http;

/**
 * RESTConnection object factory
 * @author John Boyer
 * @version 2015-07-31
 * @since 0.2
 */
public final class RESTConnectionFactory {

	/**
	 * Constructor
	 */
	private RESTConnectionFactory() {
	}
	
	/**
	 * Returns a rest connection object
	 * @return A rest connection object
	 * @throws InstantiationException if a instantiation error occurs
	 * @throws IllegalAccessException if access error occurs
	 * @throws ClassNotFoundException if a class can't be found error occurs
	 */
	public static RESTConnection getRESTConnection() 
			throws InstantiationException, 
			       IllegalAccessException, 
			       ClassNotFoundException {

		final String className = "com.rodaxsoft.http.JerseyRESTConnection";
		return (RESTConnection) Class.forName(className).newInstance();
	}

}
