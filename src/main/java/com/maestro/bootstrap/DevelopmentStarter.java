package com.maestro.bootstrap;

import com.sirra.server.*;

public class DevelopmentStarter {
	
	public static void main(String[] args)
	throws Exception
	{
		Mode.set(Mode.Development);
		HerokuStarter.main(args);
	}
}