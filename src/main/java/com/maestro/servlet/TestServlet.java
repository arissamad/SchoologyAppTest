package com.maestro.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


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
		System.out.println("SAML: " + request.getParameter("SAMLResponse"));
		
		
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
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		System.out.println("POST: This is /test.me");
		
    }
}