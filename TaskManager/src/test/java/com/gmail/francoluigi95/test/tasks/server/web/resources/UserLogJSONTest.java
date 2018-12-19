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
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserLogJSON;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserRegJSON;
import com.google.gson.Gson;

public class UserLogJSONTest {
	
	static UserRegJSON userRegJSON = new UserRegJSON();
	static UserLogJSON userLogJSON = new UserLogJSON();
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
			settings = gson.fromJson(scanner.nextLine(), UserLogJSONTest.Settings.class);
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

	// Test per il controllo delle credenziali di un utente (credenziali corrette)
	@Test
	public void test1() {
		
		// Creo un nuovo utente
		char[] pw = {'1', '2', '3', '4'};
		User1 u = new User1("davide", pw);
				
		// Creo la stringa Json
		String uString = gson.toJson(u, User1.class);
				
		try {
			// Aggiungo l'utente
			String response = gson.fromJson(userRegJSON.addUser(uString), String.class);
			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("User added: " + u.getIdentifier(), response);
		} catch (ParseException e) {
			fail();
		}
		
		// Controllo le credenziali dell'utente
		
		// Creo la stringa Json
		String uS = gson.toJson(u.getIdentifier() + ";" + String.valueOf(u.getSecret()), String.class);
		
		// Controllo le credenziali e verifico se la risposta ricevuta è uguale a quella attesa
		assertTrue(gson.fromJson(userLogJSON.checkUser(uS), Boolean.class));
	}
	
	// Test per il controllo delle credenziali di un utente (credenziali non corrette)
		@Test
		public void test2() {
			
			// Creo un nuovo utente
			char[] pw = {'5', '6', '7', '8'};
			User1 u = new User1("giovanni", pw);
					
			// Creo la stringa Json
			String uString = gson.toJson(u, User1.class);
					
			try {
				// Aggiungo l'utente
				String response = gson.fromJson(userRegJSON.addUser(uString), String.class);
				// Verifico se la risposta ricevuta è uguale a quella attesa
				assertEquals("User added: " + u.getIdentifier(), response);
			} catch (ParseException e) {
				fail();
			}
			
			// Controllo le credenziali dell'utente
			
			// Creo la stringa Json
			String uS = gson.toJson(u.getIdentifier() + ";" + "notAValidPassword", String.class);
			
			// Controllo le credenziali e verifico se la risposta ricevuta è uguale a quella attesa
			assertFalse(gson.fromJson(userLogJSON.checkUser(uS), Boolean.class));
		}

	GestoreDB g = GestoreDB.getInstance();
}
