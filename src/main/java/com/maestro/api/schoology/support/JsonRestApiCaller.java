package com.maestro.api.schoology.support;

import java.io.*;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.json.*;

import com.sirra.appcore.rest.*;

/**
 * Supports making GET, POST and PUT calls to a REST resource.
 * 
 * @author aris
 */
public class JsonRestApiCaller {
	
	protected String apiUrl;
	protected String responseBody;
	protected String body = null;
	
	protected String method;
	
	// Use this constructor when you will explicitly call the correct method, or default to POST.
	public JsonRestApiCaller(String apiUrl) {
		this.apiUrl = apiUrl;
		this.method = "post";
		
	}
	
	/**
	 * Use this constructor when you want to specify the HTTP method up front.
	 * Method can be "post", "put" or "get".
	 */
	public JsonRestApiCaller(String apiUrl, String method) {
		this.apiUrl = apiUrl;
		this.method = method.toLowerCase();
		
	}
	
	public void setMessage(Map bodyMap) {
		JSONObject j = new JSONObject(bodyMap);
		body = j.toString();
	}
	
	public void executeCall()
	throws RestException
	{
		if(method.equals("post")) {
			executePostCall();
		} else if(method.equals("get")) {
			executeGetCall();
		} else if(method.equals("put")) {
			executePutCall();
		} else if(method.equals("delete")) {
			executeDeleteCall();
		} else {
			throw new RuntimeException("Unknown HTTP method:" + method);
		}
	}
	
	public void executeGetCall()
	throws RestException
	{
		System.out.println("Making GET JSON REST API call to " + apiUrl);

		GetMethod getMethod = new GetMethod(apiUrl);
		
		try {
			getMethod.setRequestHeader("Content-type", "application/json");
			
			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Method failed: " + getMethod.getStatusLine());
			}

			// Read the response body.
			byte[] responseBodyBytes = getMethod.getResponseBody();
			responseBody = new String(responseBodyBytes);
			processResponse();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary data
			System.out.println("Received REST response:");
			System.out.println(responseBody);
			
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Status code not OK: " + statusCode);
			}
			
		} catch(HttpException e) {
			throw new RestException(e);
		} catch(IOException e) {
			throw new RestException(e);
		} finally {
			// Release the connection.
			getMethod.releaseConnection();
		}
	}
	
	public void executePostCall()
	throws RestException
	{
		System.out.println("Making POST JSON REST API call to " + apiUrl);
		PostMethod postMethod = new PostMethod(apiUrl);
		
		_executeCall(postMethod);
	}
	
	public void executePutCall()
	throws RestException
	{
		System.out.println("Making PUT JSON REST API call to " + apiUrl);
		PutMethod putMethod = new PutMethod(apiUrl);
		
		_executeCall(putMethod);
	}
	
	public void executeDeleteCall()
	throws RestException
	{
		System.out.println("Making DELETE JSON REST API call to " + apiUrl);
		DeleteMethod deleteMethod = new DeleteMethod(apiUrl);
		
		_executeCallBase(deleteMethod);
	}
	
	protected void _executeCall(EntityEnclosingMethod method) 
	throws RestException
	{
		System.out.println("Body: " + body);
		method.setRequestBody(body);
		_executeCallBase(method);
	}
	
	protected void _executeCallBase(HttpMethodBase method)
	throws RestException
	{
		try {
			method.setRequestHeader("Content-type", "application/json");
			
			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			byte[] responseBodyBytes = method.getResponseBody();
			responseBody = new String(responseBodyBytes);
			processResponse();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary data
			System.out.println("Received REST response:");
			System.out.println(responseBody);
			
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Status code not OK: " + statusCode);
			}
			
		} catch(HttpException e) {
			throw new RestException(e);
		} catch(IOException e) {
			throw new RestException(e);
		} finally {
			// Release the connection.
			method.releaseConnection();
		} 
	}

	public String getStringResponse() {
		return responseBody;
	}
	
	public JSONObject getResponseJsonObject() {
		try {
			JSONObject result = new JSONObject(responseBody);
			return result;
		} catch(JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getString(String attributeName) {
		try {
			JSONObject j = new JSONObject(responseBody);
			return (String) j.get(attributeName);
		} catch(JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void processResponse() {
		
	}
}
