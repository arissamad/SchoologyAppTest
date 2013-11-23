package com.maestro.api.schoology;

import javax.ws.rs.*;

import com.maestro.api.schoology.entity.*;
import com.sirra.appcore.sql.*;
import com.sirra.server.rest.*;

public class ExchangeRequestToken extends ApiBase {

	@GET
	public void exchange() {
		String oauthToken = getParameter("oauth_token");
		System.out.println("OAuth token is " + oauthToken);
		SchoologyToken schoologyToken = new Finder().findByField(SchoologyToken.class, "key", oauthToken);
		
		System.out.println("We found schoology token for UID: " + schoologyToken.getUid());
		
		SchoologyApi schoologyApi = new SchoologyApi();
		schoologyApi.setSchoologyToken(schoologyToken);
		
		schoologyApi.call("/oauth/access_token");
		
		String response = schoologyApi.getResponse();
		System.out.println("Response is " + response);
		
		schoologyToken.setKey(schoologyApi.getToken("oauth_token"));
		schoologyToken.setSecret(schoologyApi.getToken("oauth_token_secret"));
		schoologyToken.setIsFinalToken(true);
		
		save(schoologyToken);
	}
}