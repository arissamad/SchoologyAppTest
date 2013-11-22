package com.maestro.bootstrap;

import java.util.*;

import com.sirra.appcore.menus.*;

public class ConfigureMenus {
	
	/**
	 * Called in server bootstrap code to define menus.
	 */
	public static void configure() {
		MenuSet menuSet = MenuSet.getInstance();
		
		// First, define the the menus
		menuSet.addMenu("timer", "Timer", "TimerWidget");
		menuSet.addMenu("timesheet", "Timesheet", "TimesheetChannel");
		menuSet.addMenu("pay", "Pay", "PayChannel");
		menuSet.addMenu("users", "Users", "UsersChannel");
		menuSet.addMenu("operations", "Operations", "OperationsChannel");
		menuSet.addMenu("settings", "Settings", "SettingsChannel");
		menuSet.addMenu("account", "Account", "AccountChannel");
		menuSet.addMenu("systemadmin", "System Admin", "SystemAdminChannel");
		menuSet.addMenu("logout", "Log Out", "LogOut");
		
		// Next, define each role, and specify the menus available to each role
		menuSet.setTargetRoles("all");
		menuSet.assignMenus("logout");
		
		menuSet.setTargetRoles("user", "admin", "sirraadmin");
		menuSet.assignMenus("timer", "timesheet", "settings");
		
		menuSet.setTargetRoles("admin");
		menuSet.assignMenus("pay", "account", "users", "operations");
		menuSet.assignTags("INBOX:GlobalDelete");
		
		menuSet.setTargetRoles("sirraadmin");
		menuSet.assignMenus("systemadmin");
	}
}