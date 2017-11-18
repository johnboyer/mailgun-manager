/**
	MemberDetails.java
	
	Created by John Boyer on Jul 27, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.junit.mailgun;

import java.io.Serializable;

/**
 * Member details class
 * @author John Boyer
 * @version 2015-07-27
 * @since 0.2
 */
public final class MemberDetails implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * City
	 */
	private String city;

	/**
	 * Created date
	 */
	private String created;

	/**
	 * Province/State
	 */
	private String province;

	/**
	 * 
	 */
	public MemberDetails() {
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MemberDetails [city=" + city + ", created=" + created
				+ ", province=" + province + "]";
	}
	

}
