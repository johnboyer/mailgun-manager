/**
	MailgunAccount.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import org.apache.commons.beanutils.DynaBean;

/**
 * MailgunAccount interface
 * @author John Boyer
 * @version 2015-08-14
 * @since 0.1
 *
 */
public final class MailgunAccount {
	
	/**
	 * Base URI
	 */
	private String baseUri;
	/**
	 * Domain
	 */
	private String domain;
	/**
	 * Private API key
	 */
	private String privateApiKey;
	/**
	 * Public API key
	 */
	private String publicApiKey;
	
	public MailgunAccount() {
	}
	
	public MailgunAccount(DynaBean bean) {
		this.baseUri = (String) bean.get("baseUri");
		this.domain = (String) bean.get("domain");
		this.privateApiKey = (String) bean.get("privateApiKey");
		this.publicApiKey = (String) bean.get("publicApiKey");
	}
	
	/**
	 * @return the baseUri
	 */
	public String getBaseUri() {
		return baseUri;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @return the privateApiKey
	 */
	public String getPrivateApiKey() {
		return privateApiKey;
	}
	/**
	 * @return the publicApiKey
	 */
	public String getPublicApiKey() {
		return publicApiKey;
	}
	/**
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @param privateApiKey the privateApiKey to set
	 */
	public void setPrivateApiKey(String privateApiKey) {
		this.privateApiKey = privateApiKey;
	}
	/**
	 * @param publicApiKey the publicApiKey to set
	 */
	public void setPublicApiKey(String publicApiKey) {
		this.publicApiKey = publicApiKey;
	}
	
}
