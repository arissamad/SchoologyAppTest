package com.maestro.api.schoology;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.ws.rs.*;
import javax.xml.parsers.*;

import org.apache.commons.codec.binary.*;
import org.apache.http.*;
import org.apache.http.client.utils.*;
import org.opensaml.*;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.*;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.*;
import org.opensaml.xml.signature.*;
import org.w3c.dom.*;

import com.google.api.client.auth.oauth.*;
import com.maestro.api.schoology.entity.*;
import com.maestro.api.schoology.entity.SchoologyToken;
import com.maestro.api.schoology.support.*;
import com.sirra.appcore.rest.*;
import com.sirra.appcore.util.*;
import com.sirra.appcore.util.config.*;
import com.sirra.server.rest.*;
import com.sirra.server.session.*;

public class Login extends ApiBase {

	@POST
	public void login() {
		
		String samlResponse = getParameter("SAMLResponse");
		//System.out.println("SAML: " + samlResponse);
		
		SchoologyUser schoologyUser = null;
		
		if(samlResponse != null && samlResponse.length() > 0) {
			
			try {
				byte[] samlBytes = Base64.decodeBase64(samlResponse.getBytes());
				String samlStr = new String(samlBytes, "UTF-8");
				
				System.out.println("SAML Decoded:\n" + samlStr);
				System.out.println("\n");
				
				DefaultBootstrap.bootstrap();
				
				ByteArrayInputStream is = new ByteArrayInputStream(samlStr.getBytes());

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);
				DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

				Document document = docBuilder.parse(is);
				Element element = document.getDocumentElement();
				
				
				UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
				Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
				XMLObject responseXmlObj = unmarshaller.unmarshall(element);
				
				Response response = (Response) responseXmlObj;
				
				List<Assertion> assertions = response.getAssertions();
				
				System.out.println("Num of assertions: " + assertions.size());
				
				Assertion assertion = assertions.get(0);
				
				String subject = assertion.getSubject().getNameID().getValue();
				String issuer = assertion.getIssuer().getValue();
				
				String audience = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
				String statusCode = response.getStatus().getStatusCode().getValue();
				
				System.out.println("Subject: " + subject);
				System.out.println("Issuer: " + issuer);
				System.out.println("Audience: " + audience);
				System.out.println("Status code: " + statusCode);
				
				Signature sig = response.getSignature();
				
				System.out.println("Sig: " + sig);
				
				List<Attribute> attributes = assertion.getAttributeStatements().get(0).getAttributes();
				
				Map<String, String> attributeMap = new HashMap();
				
				for(Attribute attribute: attributes) {
					String name = attribute.getName();
					String value = attribute.getAttributeValues().get(0).getDOM().getTextContent();
					
					System.out.println("Found attribute " + name);
					System.out.println("  Value: " + value);
					
					attributeMap.put(name, value);
				}
				
				schoologyUser = new SchoologyUser(attributeMap);
				
				System.out.println("Got Schoology User!");
				//SignatureValidator validator = new SignatureValidator(credential);
				//validator.validate(sig);
				
			} catch(Exception e) {
				System.out.println("Error: " + ExceptionUtil.getStackTrace(e));
			}		
		}
		
		if(schoologyUser == null) {
			Map<String, String> testAttributeMap = new HashMap();
			
			testAttributeMap.put("school_nid", "66864031");
			testAttributeMap.put("school_title", "QuickSchools, Inc.");
			testAttributeMap.put("uid", "8527441");
			testAttributeMap.put("name_display", "Aris Samad-Yahaya");
			testAttributeMap.put("name_first", "Aris");
			testAttributeMap.put("name_last", "Samad-Yahaya");
			testAttributeMap.put("is_admin", "1");
			testAttributeMap.put("role_id", "260337");
			testAttributeMap.put("role_name", "School Admin");
			testAttributeMap.put("timezone_name", "America/Los_Angeles");
			testAttributeMap.put("domain", "schoology.schoology.com");

			schoologyUser = new SchoologyUser(testAttributeMap);
		}
		
		SchoologyToken schoologyToken = get(SchoologyToken.class, schoologyUser.getUid());
		
		if(schoologyToken == null) {
			System.out.println("Requesting new schoology access token.");
			
			SchoologyApi schoologyApi = new SchoologyApi();
			schoologyApi.call("/oauth/request_token");
			String response = schoologyApi.getResponse();
			
			schoologyToken = new SchoologyToken();
			
			schoologyToken.setUid(schoologyUser.getUid());
			schoologyToken.setKey(schoologyApi.getToken("oauth_token"));
			schoologyToken.setSecret(schoologyApi.getToken("oauth_token_secret"));
			
			save(schoologyToken);
		}
		
		if(schoologyToken.getIsFinalToken() == false) {
			// Still need to convert request token to access token.

			StringBuffer location = new StringBuffer();
			
			location.append("http://" + schoologyUser.getDomain() + "/oauth/authorize?");
			location.append("oauth_token=" + schoologyToken.getKey());
			location.append("&return_url=http://67.21.0.82/api/schoology/exchangerequesttoken");
			
			System.out.println("Redirecting to " + location.toString());
			
			try {
				SirraSession.get().getResponse().sendRedirect(location.toString());
			} catch(Exception e) {
				System.out.println("ACCESS TOKEN exchange failure: " + ExceptionUtil.getStackTrace(e));
				throw new RuntimeException(e);
			}
		}
		
		if(schoologyToken.getIsFinalToken()) {
			getUsers(schoologyUser, schoologyToken);
		}
	}
	
	protected void getUsers(SchoologyUser schoologyUser, SchoologyToken schoologyToken) {
		SchoologyApi schoologyApi = new SchoologyApi();
		schoologyApi.setSchoologyToken(schoologyToken);
		
		schoologyApi.call("/users/" + schoologyUser.getUid() + "/sections");
		
		System.out.println("Response is " + schoologyApi.getResponse());
	}
	
}
