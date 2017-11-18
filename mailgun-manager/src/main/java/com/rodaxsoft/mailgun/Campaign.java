/**
	Campaign.java
	
	Created by John Boyer on Aug 18, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

/**
 * Campaign JavaBean
 * @author John Boyer
 * @version 2015-08-18
 * @since 0.2
 */
public final class Campaign {

	/**
	 * Bounced count
	 */
	private Integer bouncedCount;
	/**
	 * Clicked count
	 */
	private Integer clickedCount;
	/**
	 * Complained count
	 */
	private Integer complainedCount;
	/**
	 * Created date
	 */
	private String createdAt;
	/**
	 * Delivered count
	 */
	private Integer deliveredCount;
	/**
	 * Dropped count
	 */
	private Integer droppedCount;
	/**
	 * ID
	 */
	private String id;
	/**
	 * Name
	 */
	private String name;
	/**
	 * Opened count
	 */
	private Integer openedCount;
	/**
	 * Submitted count
	 */
	private Integer submittedCount;
	/**
	 * Unsubscribed count
	 */
	private Integer unsubscribedCount;

	/**
	 * @return the bouncedCount
	 */
	public Integer getBouncedCount() {
		return bouncedCount;
	}

	/**
	 * @return the clickedCount
	 */
	public Integer getClickedCount() {
		return clickedCount;
	}

	/**
	 * @return the complainedCount
	 */
	public Integer getComplainedCount() {
		return complainedCount;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return the deliveredCount
	 */
	public Integer getDeliveredCount() {
		return deliveredCount;
	}

	/**
	 * @return the droppedCount
	 */
	public Integer getDroppedCount() {
		return droppedCount;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the openedCount
	 */
	public Integer getOpenedCount() {
		return openedCount;
	}

	/**
	 * @return the submittedCount
	 */
	public Integer getSubmittedCount() {
		return submittedCount;
	}

	/**
	 * @return the unsubscribedCount
	 */
	public Integer getUnsubscribedCount() {
		return unsubscribedCount;
	}
	/**
	 * @param bouncedCount the bouncedCount to set
	 */
	public void setBouncedCount(Integer bouncedCount) {
		this.bouncedCount = bouncedCount;
	}
	/**
	 * @param clickedCount the clickedCount to set
	 */
	public void setClickedCount(Integer clickedCount) {
		this.clickedCount = clickedCount;
	}
	/**
	 * @param complainedCount the complainedCount to set
	 */
	public void setComplainedCount(Integer complainedCount) {
		this.complainedCount = complainedCount;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * @param deliveredCount the deliveredCount to set
	 */
	public void setDeliveredCount(Integer deliveredCount) {
		this.deliveredCount = deliveredCount;
	}
	/**
	 * @param droppedCount the droppedCount to set
	 */
	public void setDroppedCount(Integer droppedCount) {
		this.droppedCount = droppedCount;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param openedCount the openedCount to set
	 */
	public void setOpenedCount(Integer openedCount) {
		this.openedCount = openedCount;
	}
	/**
	 * @param submittedCount the submittedCount to set
	 */
	public void setSubmittedCount(Integer submittedCount) {
		this.submittedCount = submittedCount;
	}

	/**
	 * @param unsubscribedCount the unsubscribedCount to set
	 */
	public void setUnsubscribedCount(Integer unsubscribedCount) {
		this.unsubscribedCount = unsubscribedCount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Campaign [bouncedCount=" + bouncedCount + ", clickedCount="
				+ clickedCount + ", complainedCount=" + complainedCount
				+ ", createdAt=" + createdAt + ", deliveredCount="
				+ deliveredCount + ", droppedCount=" + droppedCount + ", id="
				+ id + ", name=" + name + ", openedCount=" + openedCount
				+ ", submittedCount=" + submittedCount + ", unsubscribedCount="
				+ unsubscribedCount + "]";
	}

}
