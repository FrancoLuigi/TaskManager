package com.gmail.francoluigi95.rest.tasks.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import com.google.gson.Gson;

public class GestoreDBTest2 {

	static Gson gson = new Gson();

	class Settings {

		public String storage_base_dir; // Directory per lo storage dei task

	}

	// Test per la connessione del database (password corretta)
	@Test
	public void testDBConnection() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);
	}

	// Test per la connessione del database (password non corretta)
	@Test
	public void testDBConnection2() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, "notAValidPassword");
		assertFalse("Connection established", result);
	}

	// Test per verificare se il DB è connesso (DB connesso)
	@Test
	public void testIsDBConnected() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.isConnected();
		assertTrue("DB not connected", result);
	}

	// Test per verificare se il DB è connesso (DB non connesso)
	@Test
	public void testIsDBConnected2() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		// Chiudo la connessione
		g.closeConnection();

		result = g.isConnected();
		assertFalse("DB not connected", result);
	}

	// Test per verificare se il DB è connesso e riconettere (DB connesso)
	@Test
	public void testCheckConnectionAndReconnect() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.checkConnectionAndReconnect();
		assertTrue("DB not connected", result);
	}

	// Test per verificare se il DB è connesso e riconettere (DB non connesso)
	@Test
	public void testCheckConnectionAndReconnect2() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		// Chiudo la connessione
		g.closeConnection();

		result = g.checkConnectionAndReconnect();
		assertTrue("DB not connected", result);
	}

	// Test per chiudere la connessione al DB
	@Test
	public void testCloseConnection() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		// Chiudo la connessione
		result = g.closeConnection();
		assertTrue("DB not connected", result);
	}

	// Test per la creazione del database
	@Test
	public void testCreateDatabase() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.createDatabase();
		assertTrue("Database not created", result);
	}

	// Test per il drop database
	@Test
	public void testDropDatabase() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.createDatabase();
		assertTrue("Database not created", result);

		result = g.dropDatabase();
		assertTrue("Database not dropped", result);
	}

	// Test per la creazionde della User Table
	@Test
	public void testCreateTableUsers() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.createDatabase();
		assertTrue("Database not created", result);

		result = g.createTableUsers();
		assertTrue("User table not created", result);
	}

	// Test per la creazionde della Task table
	@Test
	public void testCreateTableTasks() {
		@SuppressWarnings("unused")
		Settings settings = null;

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest2.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException exc) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		boolean result = g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		assertTrue("Connection error", result);

		result = g.createDatabase();
		assertTrue("Database not created", result);

		result = g.createTableTasks();
		assertTrue("Task table not created", result);
	}

	GestoreDB g = GestoreDB.getInstance();
}
