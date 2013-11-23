package com.maestro.api.schoology;

import java.net.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.utils.*;

import com.google.api.client.auth.oauth.*;
import com.maestro.api.schoology.entity.*;
import com.maestro.api.schoology.support.*;
import com.sirra.appcore.util.*;
import com.sirra.appcore.util.config.*;

public class SchoologyApi {

	protected final String endpoint = "http://api.schoology.com/v1";
	protected String response;
	
	protected SchoologyToken schoologyToken;
	
	public SchoologyApi() {
		
	}
	
	public void setSchoologyToken(SchoologyToken schoologyToken) {
		this.schoologyToken = schoologyToken;
	}

	/**
	 * resourceUrl: Something like "/oauth/request_token"
	 */
	public void call(String resourceUrl) {
		String host = endpoint + resourceUrl;
		String finalUrl = makeFinalUrl(host);

		try {
			JsonRestApiCaller caller = new JsonRestApiCaller(finalUrl);
		
			caller.executeGetCall();
			
			response = caller.getStringResponse();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getResponse() {
		return response;
	}

	protected String makeFinalUrl(String url) {
		Date currDate = new Date();
		long timestamp = currDate.getTime()/1000;
		String nonce = "" + Math.round(Math.random() * 10000);

		OAuthHmacSigner signer = new OAuthHmacSigner();
		System.out.println("Signature method: " + signer.getSignatureMethod());
		
		Config config = Config.getInstance();
		
		String oauthToken = "";
		if(schoologyToken != null) {
			oauthToken = schoologyToken.getKey();
		}

		String query = "oauth_consumer_key=" + config.get("SchoologyConsumerKey") + "&" +
				"oauth_nonce=" + nonce + "&" +
				"oauth_signature_method=" + signer.getSignatureMethod() + "&" +
				"oauth_timestamp=" + timestamp + "&" +
				"oauth_token=" + oauthToken + "&" + 
				"oauth_version=" + "1.0";
		
		String baseString = "GET&" + 
				URLEncoder.encode(url) + "&" +
				URLEncoder.encode(query);
		
		try {
			signer.clientSharedSecret = config.get("SchoologyConsumerSecret");
			
			if(schoologyToken != null) {
				signer.tokenSharedSecret = schoologyToken.getSecret();
			}
			
			String signature = signer.computeSignature(baseString);
			
			System.out.println("Base string: " + baseString);
			System.out.println("Signature: " + signature);
			
			String finalUrl = url + "?" + query + "&oauth_signature=" + signature;
			
			return finalUrl;
		} catch(Exception e) {
			System.out.println("Schoology URL creation error: " + ExceptionUtil.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}
	Map<String, String> tokens;
	
	protected Map<String, String> getTokens() {
		try {	
			List<NameValuePair> params = URLEncodedUtils.parse(new URI("?" + response), "UTF-8");
			
			tokens = new HashMap();
			
			for(NameValuePair nvp: params) {
				tokens.put(nvp.getName(), nvp.getValue());
			}
			
			return tokens;
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getToken(String key) {
		if(tokens == null) getTokens();
		
		return tokens.get(key);
	}
}