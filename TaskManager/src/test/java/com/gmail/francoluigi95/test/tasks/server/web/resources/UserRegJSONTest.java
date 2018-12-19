package com.gmail.francoluigi95.test.tasks.server.web.resources;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.gmail.francoluigi95.rest.tasks.database.DBSettings;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskRegJson;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserRegJSON;
import com.google.gson.Gson;

public class UserRegJSONTest {
	
	static UserRegJSON userRegJSON = new UserRegJSON();
	static TaskRegJson taskRegJson = new TaskRegJson();
	static Gson gson = new Gson();
	
	class Settings {

		public String storage_base_dir; // Directory per lo storage dei task

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), UserRegJSONTest.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		g.createDatabase();
		g.createTableUsers();
		g.createTableTasks();

	}

	// Test per l'aggiunta di un utente
	@Test
	public void testAdd() {
		
		// Creo un nuovo utente
		char[] pw = {'1', '2', '3', '4'};
		User1 u = new User1("davide", pw);
		
		// Creo la stringa Json
		String uString = gson.toJson(u, User1.class);
		
		try {
			String response = gson.fromJson(userRegJSON.addUser(uString), String.class);
			assertEquals("User added: " + u.getIdentifier(), response);
		} catch (ParseException e) {
			fail();
		}
	}
	
	GestoreDB g = GestoreDB.getInstance();
}
