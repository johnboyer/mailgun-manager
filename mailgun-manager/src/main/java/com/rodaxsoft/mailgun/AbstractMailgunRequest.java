/**
	AbstractMailgunRequest.java
	
	Created by John Boyer on Oct 9, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractMailgunRequest class
 * @author John Boyer
 * @since 0.1
 *
 */
abstract class AbstractMailgunRequest {

	protected Map<String, String> parameters;
	
	protected AbstractMailgunRequest() {
		this.parameters = new HashMap<String, String>();
	}

	/**
	 * @return The parameters map
	 */
	Map<String, String> getParameters() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractMailgunRequest [parameters=" + parameters + "]";
	}

}
