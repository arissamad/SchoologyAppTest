package com.maestro.bootstrap;

import com.sirra.appcore.util.config.*;
import com.sirra.server.*;

/**
 * Environment variables.
 * 
 * @author aris
 */
public class InitConfig {
	
	public static void init() {
		Config config = Config.getInstance();
		
		if(Mode.get() == Mode.Development) {
			config.setPlaintextVariable("SERVER", "http://localhost:8080");
		} else {
			config.setPlaintextVariable("SERVER", "http://schoologyapptest.herokuapp.com");
		}
	}
}