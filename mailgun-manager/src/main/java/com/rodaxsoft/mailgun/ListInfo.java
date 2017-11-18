/**
	ListInfo.java
	
	Created by John Boyer on Aug 12, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

/**
 * ListInfo class
 * @author John Boyer
 * @version 2015-08-12
 *
 */
public final class ListInfo {
	/**
	 * readonly (default), members, everyone
	 */
	private String accessLevel;
	/**
	 * List address
	 */
	private String address;
	/**
	 * Member count
	 */
	private Integer count;
	/**
	 * Created date
	 */
	private String created;
	/**
	 * Description
	 */
	private String description;
	/**
	 * Name
	 */
	private String name;
	/**
	 * @return the accessLevel
	 */
	public String getAccessLevel() {
		return accessLevel;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param accessLevel the accessLevel to set
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListInfo [accessLevel=" + accessLevel + ", address=" + address
				+ ", count=" + count + ", created=" + created
				+ ", description=" + description + ", name=" + name + "]";
	}
}
