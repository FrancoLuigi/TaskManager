package com.gmail.francoluigi95.rest.tasks.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.google.gson.Gson;


public class GestoreDBTest {

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
			settings = gson.fromJson(scanner.nextLine(), GestoreDBTest.Settings.class);
			scanner.close();
			// System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		GestoreDB g = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		g.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		g.dropDatabase();
		g.createDatabase();
		g.createTableUsers();
		g.createTableTasks();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// Test inserimento e lettura di un task
	@Test
	public void testInsertReadTask() {

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task1 = new Task("task", "text", d, "new"); 

		// aggiungo il task al database
		g.insertTask(task1);

		// leggo il task dal database
		Task t = g.readTask(task1.getTitle());

		// verifico se il task letto dal db � uguale a quello inserito in precedenza
		assertEquals(task1.getTitle(), t.getTitle());
		assertEquals(task1.getState(), t.getState());
		assertEquals(task1.getText(), t.getText());
		// formatto le date per il confronto
		SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		String task1DataString = sd.format(d);
		String tDataString = sd.format(t.getDate());
		assertEquals(task1DataString, tDataString);

	}

	// test per l'aggiunta di un utente e il controllo della password dello stesso
	@Test
	public void testInsertUser() {

		// creo un nuovo utente
		char[] pw = {'1', '2', '3', '4'};
		User1 u = new User1("davide", pw);
		// aggiungo l'utente al database
		g.insertUser(u);

		// recupero la password dell'utente aggiunta in precedenza
		String password = g.readPassword(u.getIdentifier());

		// verifico che le password sono uguali
		assertEquals(password, String.valueOf(u.getSecret()));
	}

	// Test modifica di un task
	@Test
	public void testUpdateTask() {

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task1 = new Task("task1", "text1", d, "new");

		// aggiungo il task al database
		g.insertTask(task1);

		// modifico il task aggiunto in precedenza
		task1.setText("Text modified");
		task1.setState("assigned");

		// aggiorno il task nel db
		g.updateTask(task1);

		// leggo il task dal database
		Task t = g.readTask(task1.getTitle());

		// verifico se il task letto dal db � uguale a quello inserito in precedenza
		assertEquals(task1.getState(), t.getState());
		assertEquals(task1.getText(), t.getText());

	}

	// Test rimozione di un task
	@Test
	public void testDeleteTask() {

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo il nuovo task
		Task task2 = new Task("task2", "text2", d, "new");

		// aggiungo il task al database
		g.insertTask(task2);	

		// rimuovo il task nel db
		boolean result = g.deleteTask(task2.getTitle());
		assertTrue("Task not deleted", result);
	}

	// Test per il get dei task senza responsabile
	@Test
	public void testReadFreeTask() {

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo i nuovi task
		Task task3 = new Task("task3", "text3", d, "NoResponsabile"); 
		Task task4 = new Task("task4", "text4", d, "NoResponsabile");

		// aggiungo i task al database
		g.insertTask(task3);
		g.insertTask(task4);

		// recupero i task senza responsabile aggiunti in precedenza
		ArrayList<String> tasks = g.readFreeTasksTitle();
		if(tasks.contains(task3.getTitle()) && tasks.contains(task4.getTitle())){
			assertTrue("tasks not found", true);
		}

	}

	// Test per ottenere tutti i task di un utente
	@Test
	public void testReadAllTasksOfUser() {

		// creo un nuovo utente
		char[] pw = {'5', '6', '7', '8'};
		User1 u = new User1("cocca", pw);
		// aggiungo l'utente al database
		g.insertUser(u);

		// Ottengo la data corrente
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DATE, 0);
		Date d = data.getTime();

		// Creo i nuovi task
		Task task5 = new Task("task5", "text5", d, "NoResponsabile"); 
		Task task6 = new Task("task6", "text6", d, "NoResponsabile");

		// assegno i task all'utente
		task5.setUser(u.getIdentifier());
		task6.setUser(u.getIdentifier());

		// aggiungo i task al database
		g.insertTask(task5);
		g.insertTask(task6);


		// ottengo tutti i task dell'utente
		ArrayList<String> tasks = g.readAllTasksOfUser(u.getIdentifier());
		if(tasks.contains(task5.toString()) && tasks.contains(task6.toString())) {
			assertTrue("tasks not found", true);
		}
		else {
			fail();
		}

	}

	GestoreDB g = GestoreDB.getInstance();
}
