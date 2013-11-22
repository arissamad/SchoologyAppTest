package com.maestro.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.apache.commons.codec.binary.*;


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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		System.out.println("GET: This is /test.me");
		
		System.out.println("SN: " + request.getServerName());
		System.out.println("URL: " + request.getRequestURL().toString());
		System.out.println("URI: " + request.getRequestURI());
		System.out.println("PI: " + request.getPathInfo());
		
		String samlResponse = request.getParameter("SAMLResponse");
		System.out.println("SAML: " + samlResponse);
		
		if(samlResponse != null && samlResponse.length() > 0) {
			byte[] samlBytes = Base64.decodeBase64(samlResponse.getBytes());
			String samlStr = new String(samlBytes, "UTF-8");
			
			System.out.println("SAML Decoded:\n" + samlStr);
			System.out.println("\n");
		}
		
		Enumeration<String> en = request.getParameterNames();
		
		while(en.hasMoreElements()) {
			String name = en.nextElement();
			System.out.println("  Name: " + name);
			System.out.println("  Value: " + request.getParameter(name));
		}
		
		System.out.println("\nHeaders");
		Enumeration<String> headers = request.getHeaderNames();
		
		while(headers.hasMoreElements()) {
			String name = headers.nextElement();
			System.out.println("  HEADER name: " + name);
			System.out.println("  HEADER value: " + request.getHeader(name));
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