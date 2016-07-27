[![Build Status](https://travis-ci.org/johnboyer/mailgun-manager.svg?branch=master)](https://travis-ci.org/johnboyer/mailgun-manager) [![License](https://img.shields.io/badge/license-MPL%202.0-orange.svg)](https://www.mozilla.org/en-US/MPL/2.0)

# Mailgun Manager for Java  Version 0.25
*Updated Wed, Jul 27, 2016*

## Introduction
Mailgun Manager API project is a _partial_ Java library implementation of [Mailgun’s API](http://documentation.mailgun.com/api_reference.html#api-reference). It provides a single interface to perform common Mailgun API operations and simplifies access by abstracting the lower level HTTP programming code.

Mailgun Manager makes easy to:

* Send messages to mailing lists or individuals
* Get campaigns and their history (events)
* Save campaign events to a CSV file
* Add members to a mailing list (one a time or in-bulk)
* Fetch, delete, unsubscribe, and update mailing list members
* Discover invalid email addresses unidentified by other libraries such as `EmailValidator` in Apache Commons

## Setup
Before performing API operations for the first time, you'll need to either configure Mailgun in the `mailgun.properties` file or setup an account programmatically.

### Configure Properties File (Preferred)

In the root of your classpath or source directory, create a file named, `mailgun.properties`. Set the `publicApiKey`, `privateApiKey`, and the `domain` property values. The API keys can be found in your Mailgun's account settings.

	# mailgun.properties file
	# Mailgun account properties

	baseUri = https://api.mailgun.net/v3
	publicApiKey = pubkey-XXXXX
	privateApiKey = key-XXXXXX
	domain = mg.example.com

### Configure Programmatically
Configure a `MailgunAccount` object and invoke the `MailgunManager.register(MailgunAccount)` method.

	//Register Mailgun account info
	MailgunAccount account = new MailgunAccount();
	account.setBaseUri("https://api.mailgun.net/v3");
	account.setDomain("mg.example.com");
	account.setPrivateApiKey("key-XXXXXX");
	account.setPublicApiKey("pubkey-XXXXX");	

	MailgunManager.register(account);

## Usage
Using the Mailgun Manager library is easy.

### Email Validation
	
	//Validate an email address
	EmailValidationResponse response;
	response = MailgunManager.isValidEmail("john@example.com");
	
	//Query the response
	if(!response.isValid()) {
		//Do something the email is invalid
		
		final String didYouMean = response.getDidYouMean();
		if(didYouMean != null) {
			//Ask if the meant the value of didYouMean
		}
	}
	
### Mailing Lists
#### Fetch Lists
	Collection<ListInfo> lists = MailgunManager.getMailingLists();

#### Add Member

	JSONObject objects = new JSONObject();
	objects.put("city", "Seattle");
	objects.put("created", DateFormat.getInstance().format(new Date()));
	objects.put("province", "WA");
		
	ListMemberRequest member = new ListMemberRequest()
	                                .setAddress("john@example.com")
	                                .setName(" John Doe")
	                                .setJSONVar(objects.toString());
		
	boolean success = MailgunManager.addMailingListMember("list@mg.example.com", member)
	
#### Add Multiple Members
Unlike the API, Mailgun Manager will accept adding `> 1000` members at a time.

	List<ListMember> members = ...
	boolean success = MailgunManager.addMailingListMembers("list@mg.example.com", members)
	
#### Fetch a Member
	ListMemberRequest request = ...
	boolean success = MailgunManager.addMailingListMember(sMailingListAddress, request);

#### Fetch Members
##### Without Arbitrary JSON

	List<ListMember> members = getMailingListMembers("list@mg.example.com");

##### With Arbitrary JSON
1. Write an Apache Commons BeanUtils [Converter](http://commons.apache.org/proper/commons-beanutils/javadocs/v1.9.2/apidocs/org/apache/commons/beanutils/Converter.html) class for the *var* object.
2. Register the converter
3. Then call with the var type Class object: `getMailingListMembers(String, Class)`

   		///////////////////////////////////////
		//Fetch members of a mailing list with a var object
		///////////////////////////////////////

		//Write an Apache Commons BeanUtils Converter for the var object
		Converter varConverter = new org.apache.commons.beanutils.Converter() {

			@Override
			public <T> T convert(Class<T> type, Object value) {

				MyVarObject var = null;

				if (value instanceof JSONObject) {

					JSONObject obj = (JSONObject) value;
					var = new MyVarObject();

					try {
						BeanUtils.populate(var, obj);
					} catch (IllegalAccessException | InvocationTargetException e) {
						ContextedRuntimeException cre;
						cre = new ContextedRuntimeException("Bean population error", e);
						cre.addContextValue("value", obj);
						throw cre;
					}

				}

				return type.cast(var);
			}
		};	
    
    	//Register the converter
    	ConvertUtils.register(varConverter, MyVarObject.class)
	
		List<ListMember> members = getMailingListMembers("list@mg.example.com", MyVarObject.class);

#### Update a Member

	ListMemberRequest request = ...
	//Change the name
	request.setName("John Updated");
	boolean success = MailgunManager.updateMailingListMember(sMailingListAddress, request);

#### Delete a Member

	boolean success;
	success = MailgunManager.deleteMailingListMember("list@mg.example.com", "john@example.com")
	
### Messages
			
	///////////////////////////////////////
	//Send a email
	/////////////////////////////////////////
	EmailRequest email = new EmailRequest()
		                         .setTo("mary@example.com")
								 .setFrom("john@example.com")
								 .setSubject("Hello")
								 .setTextBody("Hello Mary:\n\nThis message was sent from Mailgun Manager for Java.")
								 .setHeader("X-Test-Class", getClass().getSimpleName());
	
	boolean success = MailgunManager.sendMessage(email);

### Campaigns

#### Fetch Campaign
	
	Campaign campaign = MailgunManager.getCampaign("myCampaignId");

#### Fetch Events

	List<Map<String, Object>> events;
	events = MailgunManager.getCampaignEvents("myCampaignId");
	
#### Save Events to a CSV File

	MailgunManager.saveCampaignEventsToCSV("myCampaignId");
		
## Motivation
I created this library primarily for two reasons:

1. I had a hard time finding a Java implementation of the Mailgun API. 
2. I was unable to find examples using the latest version of the [Jersey framework](https://jersey.java.net).

In any case, this project is a work-in-progress.

## Installation

### Prerequisites
1. Install and configure [Eclipse IDE for Java EE Developers](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr1)
2. Install and configure the [Egit](http://www.eclipse.org/egit) plugin (optional)
3. Install and configure the [Buildship Eclipse Plugin](https://projects.eclipse.org/projects/tools.buildship) (optional)

This project uses [Gradle](https://gradle.org) for build automation. It's dependent on the following libraries:

* commons-collections4 4.1
* commons-configuration 2.0
* commons-lang3 3.4
* commons-logging 1.2
* commons-validator 1.5
* commons-beanutils 1.9
* commons-io 2.5
* commons-csv 1.4
* jersey-client 2.23
* json-lib 2.4 (jdk15)
* javax.mail 1.5

### Import Project
The following steps assume that you're using Eclipse with the [Egit](http://www.eclipse.org/egit) plugin.

1. In Eclipse, click File > Import > Git > Projects from Git.
2. Then click Next > Clone URI, set the Connection to Git, set the URI, and click Next > Next > Next > Finish.
3. Click Import existing projects, click Working Directory, and click Next > Finish.


## API Reference

The `MailgunManager` class in the `com.rodaxsoft.mailgun` package is a facade class that implements the following methods: 

* `addMailingListMember(String, ListMemberRequest)`
* `addMailingListMembers(String, List<ListMember>)`
* `deleteMailingListMember(String, String)`
* `getCampaign(String)`
* `getCampaignEvents(String)`
* `getMailingListMember(String, String)`
* `getMailingListMembers(String)`
* `getMailingListMembers(String, Class<?>)` 
* `getMailingLists()`
* `isValidEmail(String)`
* `register(MailgunAccount)`
* `saveCampaignEventsToCSV(String)`
* `sendMessage(EmailRequest)`
* `unsubscribeMailingListMember(String, String)`
* `updateMailingListMember(String, ListMemberRequest)`

*Note: Clients must register Mailgun account info before invoking a Mailgun API operation such as adding a member to a mailing list.*

See the Javadoc comments in `MailgunManager.java` for more details.

## Tests

See the test cases in the `com.rodaxsoft.junit.mailgun` package.

Before running, edit the properties in `testcase.properties` file

# Mailgun account properties

	baseUri = https://api.mailgun.net/v3
	publicApiKey = pubkey-XXXXX
	privateApiKey = key-XXXXXX
	domain = mg.example.com

	# Mailing List
	mailing.list = test@mg.example.com
	email.from = postmaster@mg.example.com
	email.to = john@example.com

	#Campaign
	campaign.id = myid
	
## Contributors

To reach a milestone for a major release, we'd like contributions for the following:
* Create, update, fetch, and delete a campaign
* Fetch campaign stats, clicks, opens, unsubscribes, and complaints
* Fetch and delete stored messages
* Create, update, and delete mailing lists

Mailgun API routines are implemented as subclasses of the `AbstractMailgunRoutine` class. Current implementation class examples include: `EmailValidator`, `MailSender`, and `MailingListManager`.

Contributions can be made by following these steps:

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

If you have any questions, please don't hesitate to contact me at john@rodaxsoft.com.

## License
This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
