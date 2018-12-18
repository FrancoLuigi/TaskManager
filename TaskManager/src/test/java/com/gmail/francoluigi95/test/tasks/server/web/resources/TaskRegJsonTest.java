package com.gmail.francoluigi95.test.tasks.server.web.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.database.DBSettings;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskRegJson;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TaskRegJsonTest {

	static TaskRegJson TaskRegJson = new TaskRegJson();
	static Gson gson = new Gson();

	class Settings {

		public String storage_base_dir; // Directory per lo storage delle note

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		@SuppressWarnings("unused")
		Settings settings = null;

		try {

			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"), "UTF-8");
			settings = gson.fromJson(scanner.nextLine(), TaskRegJsonTest.Settings.class);
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

	// Test per l'aggiunta di un task (non presente nel database)
	@Test
	public void testAdd1() {

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task1 = new Task("task", "text", d, "new");

		// Creo la stringa Json
		String task1String = gson.toJson(task1, Task.class);

		try {
			// Aggiungo il task
			String response = gson.fromJson(TaskRegJson.addTask(task1String), String.class);

			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("Task added: " + task1.getTitle(), response);

		} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
			fail();
		}

	}

	// Test per l'aggiunta di un task con data precedente a quella attuale

	// Test per l'aggiunta di un task con data precedente a quella attuale
	@Test
	public void testAdd2() {
		// Ottengo la data di ieri
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, -1);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task1 = new Task("task", "text", d, "new");

		// Creo la stringa Json
		String task1String = gson.toJson(task1, Task.class);

		try {
			// Aggiungo il task
			String response = gson.fromJson(TaskRegJson.addTask(task1String), String.class);

			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("The deadline date is earlier than today", response);

		} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
			fail();
		}

	}

	// Test per l'aggiunta di un task con il titolo che inizia con uno spazio
	@Test
	public void testAdd3() {
		// Ottengo la data di oggi
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task1 = new Task(" task", "text", d, "new");

		// Creo la stringa Json
		String task1String = gson.toJson(task1, Task.class);

		try {
			// Aggiungo il task
			String response = gson.fromJson(TaskRegJson.addTask(task1String), String.class);

			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("Re-enter the title", response);

		} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
			fail();
		}

	}

	GestoreDB g = GestoreDB.getInstance();
}