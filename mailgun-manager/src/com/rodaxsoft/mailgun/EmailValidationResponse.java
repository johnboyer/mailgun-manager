/**
	EmailValidationResponse.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

/**
 * EmailValidationResponse class
 * @author John Boyer
 * @since 0.1
 */
public final class EmailValidationResponse {

	static final String DID_YOU_MEAN_KEY = "did_you_mean";
	static final String IS_VALID_KEY = "is_valid";
	
	private final String address;
	private final String didYouMean;
	private final boolean isValid;

	EmailValidationResponse(String address, boolean isValid, String didYouMean) {
		this.address = address;
		this.isValid = isValid;
		this.didYouMean = didYouMean;
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the didYouMean
	 */
	public String getDidYouMean() {
		return didYouMean;
	}
	

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailValidationResponse [address=" + address + ", didYouMean="
				+ didYouMean + ", isValid=" + isValid + "]";
	}

}
