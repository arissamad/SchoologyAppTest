package com.maestro.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;

import org.apache.commons.codec.binary.*;
import org.opensaml.*;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.*;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.*;
import org.opensaml.xml.signature.*;
import org.w3c.dom.*;

import com.sirra.appcore.util.*;


/**
 * Any request path that is not recognized by another servlet (such as ApiServlet) will come here.
 * 
 * This will serve static content located in resources/publicweb.
 * 
 * @author aris
 *
 */
@WebServlet(urlPatterns = {"/test.me"})
public class TestServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response2) throws ServletException, IOException
    {
		System.out.println("GET: This is /test.me");
		
		System.out.println("SN: " + request.getServerName());
		System.out.println("URL: " + request.getRequestURL().toString());
		System.out.println("URI: " + request.getRequestURI());
		System.out.println("PI: " + request.getPathInfo());
		
		String samlResponse = request.getParameter("SAMLResponse");
		//System.out.println("SAML: " + samlResponse);
		
		if(samlResponse != null && samlResponse.length() > 0) {
			byte[] samlBytes = Base64.decodeBase64(samlResponse.getBytes());
			String samlStr = new String(samlBytes, "UTF-8");
			
			System.out.println("SAML Decoded:\n" + samlStr);
			System.out.println("\n");
			
			try {
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
				
				//SignatureValidator validator = new SignatureValidator(credential);
				//validator.validate(sig);
				
			} catch(Exception e) {
				System.out.println("Error: " + ExceptionUtil.getStackTrace(e));
			}
		}
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			
			if(reader != null) {
				while ((line = reader.readLine()) != null) {
				      jb.append(line);
				}
			}
			
		} catch (Exception e) { /*report an error*/ }

		System.out.println("POST body: " + jb.toString());
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		System.out.println("POST: This is /test.me");
		doGet(request, response);
    }
}