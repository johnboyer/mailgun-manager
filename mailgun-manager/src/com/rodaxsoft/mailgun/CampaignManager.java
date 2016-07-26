/**
	CampaignManager.java
	
	Created by John Boyer on Aug 18, 2015
	(c) Copyright 2015 Rodax Software, Inc. All Rights Reserved. 

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. 
 */
package com.rodaxsoft.mailgun;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedException;

import com.rodaxsoft.http.RESTResponse;
import com.rodaxsoft.mailgun.converters.CampaignConverter;

/**
 * CampaignManager class
 * @author John Boyer
 * @version 2015-08-18
 * @since 0.2
 */
final class CampaignManager extends AbstractMailgunRoutine {

	/**
	 * Constructor
	 * @param account Mailgun account info
	 */
	protected CampaignManager(MailgunAccount account) {
		super(account);
		
		ConvertUtils.register(new CampaignConverter(), Campaign.class);
	}
	
	/**
	 * Returns the campaign for the given ID
	 * @param campaignId The campaign ID
	 * @return the campaign for the given ID or <code>null</code>
	 * @throws ContextedException if a processing error occurs
	 */
	Campaign getCampaign(String campaignId) throws ContextedException {
		final String domain = getAccount().getDomain();
		final String path =   "/" + domain + "/campaigns/" + campaignId;
		RESTResponse restResponse = invokeGet(path);
		
		Campaign campaign = null;
		if(restResponse.getFamily() == SUCCESSFUL) {
			JSONObject json = toJSONObject(restResponse.getStringResponse());
			campaign = (Campaign) ConvertUtils.convert(json, Campaign.class);
		}
		
		return campaign;
		
	}

	/**
	 * Returns campaign events as list of maps
	 * @param campaignId The campaign ID for the events
	 * @return A list of campaign events as map objects or <code>null</code>
	 * @throws ContextedException if a processing error occurs
	 * @since 0.2
	 */
	@SuppressWarnings("unchecked")
	List<Map<String, Object>> getEvents(String campaignId) throws ContextedException {
		final String domain = getAccount().getDomain();
		final String path = "/" + domain + "/campaigns/" + campaignId + "/events";
		
		RESTResponse restResponse = invokeGet(path);
		//Parse response
		List<Map<String, Object>> events = null;
		if(restResponse.getFamily() == SUCCESSFUL) {
			
			JSONArray array = JSONArray.fromObject(restResponse.getStringResponse());
			events = new ArrayList<>();
			events.addAll(array);
			
		}
		
		return events;
	}
	
	/**
	 * Saves campaign events to a CSV file with the following format:
	 * <code>&lt;campaign name&gt;_(&lt;campaign id&gt;)_&lt;timestamp&gt;.csv</code>
	 * @param campaignId The campaign ID
	 * @throws ContextedException if a processing error occurs
	 * @throws IOException if an I/O error occurs
	 */
	void saveCampaignEventsToCSV(String campaignId) 
			throws ContextedException, IOException {
		
		Campaign campaign = getCampaign(campaignId);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		String dateTime = format.format(new Date());
		String fileName;
		
		if(campaign != null) {

			String name = StringUtils.replace(campaign.getName(), " ", "_");
			fileName = new StringBuilder(name)
					           .append("_(")
			                   .append(campaignId)
			                   .append(")_")
			                   .append(dateTime)
			                   .append(".csv")
			                   .toString();			
		}
		else {
			fileName = campaignId + "_" + dateTime + ".csv";
		}
		
		
		CSVPrinter csvPrinter = null;
		PrintWriter pw = null;
		CSVFormat csvFormat = null;
		try {

			pw = new PrintWriter(fileName);
			final List<Map<String, Object>> events = getEvents(campaignId);
			
			
			for (Map<String, Object> map : events) {

				if (null == csvPrinter) {
					final Set<String> keySet = map.keySet();
					int size = keySet.size();
					String[] keys = keySet.toArray(new String[size]);
					csvFormat = CSVFormat.DEFAULT.withHeader(keys);
					csvPrinter = new CSVPrinter(pw, csvFormat);
				}
//				city	domain	tags	timestamp	region	ip	country	recipient	event	user_vars

				String[] headers = csvFormat.getHeader();
				for (String key : headers) {
					csvPrinter.print(map.get(key));
				}
				
				csvPrinter.println();
			}
			
		} finally {

			if (csvPrinter != null) {
				csvPrinter.flush();
			}

			IOUtils.closeQuietly(csvPrinter);
		}

	}
}
