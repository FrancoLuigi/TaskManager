package com.gmail.francoluigi95.test.tasks.server.web.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.database.DBSettings;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskRegJson;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class TaskRegJsonTest {

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
			String response = gson.fromJson(taskRegJson.addTask(task1String), String.class);

			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("Task added: " + task1.getTitle(), response);

		} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
			fail();
		}

	}

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
			String response = gson.fromJson(taskRegJson.addTask(task1String), String.class);

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
			String response = gson.fromJson(taskRegJson.addTask(task1String), String.class);

			// Verifico se la risposta ricevuta è uguale a quella attesa
			assertEquals("Re-enter the title", response);

		} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
			fail();
		}

	}
	
	// Test per l'update di un task
		@Test
		public void testUpdate() {
			
			// Aggiungo un nuovo task
			
			// Ottengo la data di oggi
			Calendar data = Calendar.getInstance();
			data.add(Calendar.DATE, 0);
			Date d = data.getTime();

			// Creo il nuovo task
			Task task1 = new Task("task", "text", d, "new");

			// Creo la stringa Json
			String task1String = gson.toJson(task1, Task.class);

			try {
				// Aggiungo il task
				String response = gson.fromJson(taskRegJson.addTask(task1String), String.class);

				// Verifico se la risposta ricevuta è uguale a quella attesa
				assertEquals("Task added: " + task1.getTitle(), response);

			} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
				fail();
			}
			
			// Modifico il task aggiunto in precedenza
			task1.setText("new_text");
			task1.setState("assigned");

			data.add(Calendar.DATE, 1);
			d = data.getTime();
			task1.setDate(d);
			
			// Creo la stringa Json
			task1String = gson.toJson(task1, Task.class);
			
			try {
				// Aggiorno il task
				String response = gson.fromJson(taskRegJson.updateTask(task1String), String.class);

				// Verifico se la risposta ricevuta è uguale a quella attesa
				assertEquals("Task modified: " + task1.getTitle(), response);

			} catch (JsonSyntaxException | ParseException | InvalidUsernameException | FileNotFoundException e) {
				fail();
			}

		}
		
		// Test per il get dei task senza responsabile
				@Test
				public void testGetTasks() {
					
					// Aggiungo dei nuovi task
					
					// Ottengo la data di oggi
					Calendar data = Calendar.getInstance();
					data.add(Calendar.DATE, 0);
					Date d = data.getTime();

					// Creo il nuovo task
					Task task1 = new Task("task1", "text1", d, "noResponsabile");

					// Creo la stringa Json
					String task1String = gson.toJson(task1, Task.class);

					try {
						// Aggiungo il task
						String response = gson.fromJson(taskRegJson.addTask(task1String), String.class);

						// Verifico se la risposta ricevuta è uguale a quella attesa
						assertEquals("Task added: " + task1.getTitle(), response);

					} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
						fail();
					}
					
					// Creo il nuovo task
					Task task2 = new Task("task2", "text2", d, "noResponsabile");

					// Creo la stringa Json
					String task2String = gson.toJson(task2, Task.class);

					try {
						// Aggiungo il task
						String response = gson.fromJson(taskRegJson.addTask(task2String), String.class);

						// Verifico se la risposta ricevuta è uguale a quella attesa
						assertEquals("Task added: " + task2.getTitle(), response);

					} catch (JsonSyntaxException | ParseException | InvalidUsernameException e) {
						fail();
					}
					
					// Ottengo tutti i task senza responsabile
					ArrayList<String> tasks;
					try {
						// Get tasks
						tasks = gson.fromJson(taskRegJson.getTasks(), ArrayList.class);

						// Verifico se è presente il task1
						boolean task1found = false;
						for (String i : tasks) {
							if (i.equals(task1.getTitle())) {
								assertTrue(true);
								task1found = true;
							}
						}
						if (task1found == false) {
							fail();
						}
						
						// Verifico se è presente il task2
						boolean task2found = false;
						for (String i : tasks) {
							if (i.equals(task2.getTitle())) {
								assertTrue(true);
								task2found = true;
							}
						}
						if (task2found == false) {
							fail();
						}

					} catch (JsonSyntaxException | ParseException | InvalidKeyException e) {
						fail();
					}
					
				}

	GestoreDB g = GestoreDB.getInstance();
}
