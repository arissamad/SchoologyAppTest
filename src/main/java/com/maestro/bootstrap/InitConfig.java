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
			
			config.setPlaintextVariable("SchoologyConsumerKey", "ee4664e81b89d2ffb9756b319feba534052663b3c");
			config.setPlaintextVariable("SchoologyConsumerSecret", "416825aea7611325c2188be17ffc7873");
		} else {
			config.setPlaintextVariable("SERVER", "http://schoologyapptest.herokuapp.com");
			
			config.setPlaintextVariable("SchoologyConsumerKey", "ee4664e81b89d2ffb9756b319feba534052663b3c");
			config.setPlaintextVariable("SchoologyConsumerSecret", "416825aea7611325c2188be17ffc7873");
		}
	}
}