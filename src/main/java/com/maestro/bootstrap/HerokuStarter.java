package com.maestro.bootstrap;

import org.eclipse.jetty.annotations.*;
import org.eclipse.jetty.plus.webapp.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;

import com.maestro.api.accounts.entity.*;
import com.maestro.api.users.entity.*;
import com.sirra.server.*;
import com.sirra.server.persistence.*;
import com.sirra.server.rest.*;
import com.sirra.appcore.firebase.*;
import com.sirra.appcore.plans.*;
import com.sirra.appcore.sql.*;

public class HerokuStarter {
	
	public static void main(String[] args)
	throws Exception 
	{
		System.out.println("Starting SchoologyAppTest... ");
		System.out.println("Mode: " + Mode.get().name());

    	// Initialize the Config environment variables
    	InitConfig.init();
    	
    	if(args.length > 0 && args[0].equals("nohibernate")) {
    		System.out.println("Not starting hibernate.");
    	} else {
    		HibernateStarter.init("com.maestro");
    	}
    	
    	// Set base package for API classes
    	ApiServlet.setAPIPackageBase("com.maestro");
    	
    	// Initialize Finder
    	Finder.configure(Account.class, User.class);
    	
    	// Configure plans
    	Plan smallPlan = new Plan("small", "Small", 7, 9.95);
    	Plan.configure(smallPlan, smallPlan);
    	
    	// Set firebase location
    	Firebase.setFirebaseInstance("schoologyapptest");
    	
    	// Define the menus
    	ConfigureMenus.configure();
    	
    	String webPort = System.getenv("PORT");
        int port = isBlank(webPort) ? 8080 : Integer.parseInt(webPort);
        
		Server server = new Server(port);

		String wardir = "target/schoologyapptest-1.0/";

		WebAppContext context = new WebAppContext();
		
		context.setResourceBase(wardir);
		context.setDescriptor(wardir + "WEB-INF/web.xml");
		
		context.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new WebXmlConfiguration(),
				new WebInfConfiguration(), new TagLibConfiguration(),
				new PlusConfiguration(), new MetaInfConfiguration(),
				new FragmentConfiguration(), new EnvConfiguration() });

		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		
		server.setHandler(context);
		server.start();
		server.join();
		
		System.out.println("SchoologyAppTest has terminated.");
	}
	
	private static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
}
