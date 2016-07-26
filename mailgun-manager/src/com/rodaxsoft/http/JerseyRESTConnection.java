/**
	JerseyRESTConnection.java
	
	Created by John Boyer on Oct 6, 2014
	(c) Copyright 2014 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.http;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * JerseyRESTConnection class abstracts Jersey client API
 * @author John Boyer
 * @version 2015-08-04
 * @since 0.1
 *
 */
class JerseyRESTConnection implements RESTConnection {
	
	/**
	 * Jersey response implementation class
	 * @author John Boyer
     * @version 2015-08-20
	 */
	private static class JerseyRESTResponse implements RESTResponse {
		private Family family;
		private String response;
		
		/**
		 * Constructor
		 * @param response A string response
		 * @param family The family enum object
		 */
		JerseyRESTResponse(String response, Family family) {
			this.response = response;
			this.family = family;
		}

		@Override
		public Family getFamily() {
			return family;
		}

		@Override
		public String getStringResponse() {
			return response;
		}

		@Override
		public JSONObject toJSONObject() {
			return JSONObject.fromObject(response);
		}

		@Override
		public boolean success() {
			return family == Family.SUCCESSFUL;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "JerseyRESTResponse [family=" + family + ", response=" + response
					+ "]";
		}
		
	}

	/**
	 * Form object
	 */
	private Form form;
	/**
	 * Invocation Builder instance
	 */
	private Builder invocationBuilder;
	/**
	 * Logging object
	 */
	private final Log log;
	/**
	 * Web Target
	 */
	private WebTarget webTarget;


	/**
	 * Constructs a REST connection object
	 */
	public JerseyRESTConnection() {
		log = LogFactory.getLog(getClass());
	}

	@Override
	public RESTConnection addFormParams(Map<String, String> params) {		
		form = new Form(new MultivaluedHashMap<>(params));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#addPath(java.lang.String)
	 */
	@Override
	public RESTConnection addPath(String path) {
		if(path != null) {
			log.debug("Path = " + path);
			webTarget = webTarget.path(path);
		}

		return this;
	}

	
	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#addQueryParam(java.lang.String, java.lang.String)
	 */
	@Override
	public RESTConnection addQueryParam(String name, String value) {
		if(name != null && value != null) {
			log.debug("Query Parm: " + "name = ," + name + " value = " + value);
			webTarget = webTarget.queryParam(name, value);
		}
		
		return this;
	}

	
	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#configureRequest(java.lang.String)
	 */
	@Override
	public RESTConnection configureRequest(String mediaType) {
		invocationBuilder = webTarget.request(MediaType.valueOf(mediaType));
		return this;
	}

	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#connect(java.lang.String, java.lang.String)
	 */
	@Override
	public RESTConnection connect(String baseUri, String apiKey) {
		final Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature authFeature;
		authFeature = HttpAuthenticationFeature.
				basicBuilder().
				nonPreemptive().
				credentials("api", apiKey).
				build();
		client.register(authFeature);
		
		webTarget = client.target(baseUri);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#invokeGet()
	 */
	@Override
	public RESTResponse invokeGet() {	
		validate();
		Response response = invocationBuilder.get();
		return new JerseyRESTResponse(readResponse(response), response.getStatusInfo().getFamily());
	}

	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#invokeDelete()
	 */
	@Override
	public RESTResponse invokeDelete() {
		validate();
		Response response = invocationBuilder.delete();
		return new JerseyRESTResponse(readResponse(response), 
				response.getStatusInfo().getFamily());
	} 
	
	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#invokePost()
	 */
	@Override
	public RESTResponse invokePost() {
		validate();
		Response response =   invocationBuilder.post(Entity.form(form));
		return new JerseyRESTResponse(readResponse(response), 
				response.getStatusInfo().getFamily());
	}
	
	/* (non-Javadoc)
	 * @see com.rodaxsoft.http.RESTConnection#invokePut()
	 */
	@Override
	public RESTResponse invokePut() {
		validate();
		Response response = invocationBuilder.put(Entity.form(form));
		return new JerseyRESTResponse(readResponse(response), 
				response.getStatusInfo().getFamily());
	}
	
	/**
	 * Reads the response and returns a string representation
	 * @param response The response object
	 * @return A string representation
	 */
	private String readResponse(Response response) {
		log.debug("Status: " + response.getStatus());
		
		String entity = null;
		if(response.hasEntity()) {
			entity = response.readEntity(String.class);	
		}

		log.debug("Repsonse: \n" + entity);
		log.debug(response.getStatusInfo().getFamily().name());
		return entity;
	}

	/**
	 * Validates the <code>invocationBuilder</code>
	 */
	private void validate() {
		if (null == invocationBuilder) {
			ContextedRuntimeException cre = new ContextedRuntimeException("Must configure request before invoking");
			cre.addContextValue("URI", webTarget.getUri());
			cre.addContextValue("target", webTarget.toString());
			throw cre;

		}
	}

}
