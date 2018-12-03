package com.gmail.francoluigi95.rest.tasks.server.web.frontend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import com.gmail.francoluigi95.rest.tasks.database.DBSettings;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskJSON;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskRegJson;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserLogJSON;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserRegJSON;
import com.google.gson.Gson;

public class TaskRegistryWebApp extends Application {

	/* Creo la classe relative alle impostazioni del server */

	private class Settings {
		public int port; // Porta utilizzata dal server sulla quale fornisce i propri servizi
		public String web_base_dir; // directory per le risorse web
	}

	private static String rootDirForWebStaticFiles;

	public Restlet createInboundRoot() {
		Router router = new Router(getContext()); // Creo il Router per l'indirizzamento delle risorse tramite URI

		Directory directory = new Directory(getContext(), rootDirForWebStaticFiles);
		directory.setListingAllowed(true);
		directory.setDeeplyAccessible(true);

		router.attach("/TaskRegApplication/tasks", TaskRegJson.class);
		router.attach("/TaskRegApplication/freeTasks", TaskRegJson.class);
		router.attach("/TaskRegApplication/allTasks/{username}", UserRegJSON.class);
		router.attach("/TaskRegApplication/tasks/{title}", TaskJSON.class);
		router.attach("/TaskRegApplication/login", UserLogJSON.class);
		router.attach("/TaskRegApplication/registration", UserRegJSON.class);

		return router;
	}

	public static void main(String[] args) {

		Gson gson = new Gson();
		Settings settings = null;

		// Inizializzo le impostazioni del server basandomi sulle informazioni contenute
		// nel file settings.json

		try {
			Scanner scanner = new Scanner(new File("src/main/resources/settings.json"));
			settings = gson.fromJson(scanner.nextLine(), Settings.class);
			scanner.close();
			System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		rootDirForWebStaticFiles = "file:///" + System.getProperty("user.dir") + "//" + settings.web_base_dir;
		System.err.println("Web Directory: " + rootDirForWebStaticFiles);

		gestoreDB = GestoreDB.getInstance();
		DBSettings.readSettingsFromFile();
		gestoreDB.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);

		Scanner scanner = new Scanner(System.in);
		while (!gestoreDB.isConnected()) {
			System.out.println("\nInserisci i dati di configurazione per riprovare a connetterti.");
			System.out.print("Inserisci l'host (in genere: localhost): ");
			String host = scanner.nextLine();
			System.out.print("Inserisci la porta (in genere: 3306): ");
			String port = scanner.nextLine();
			System.out.print("Inserisci l'username:");
			String user = scanner.nextLine();
			System.out.print("Inserisci la password:");
			String pass = scanner.nextLine();
			DBSettings.updateSettingsInFile(host, port, user, pass);
			gestoreDB.connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass);
		}

		scanner.close();

		gestoreDB.createDatabase();
		gestoreDB.createTableUsers();
		gestoreDB.createTableTasks();

		try {
			Component component = new Component(); // Creo la componente Restlet
			component.getServers().add(Protocol.HTTP, settings.port); // Imposto le informazioni relative al Server
			component.getClients().add(Protocol.FILE); // Imposto le informazioni relative al Client
			component.getDefaultHost().attach(new TaskRegistryWebApp()); // Allego l'Application al DefaultHost

			// Avvio la componente Restlet
			component.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static GestoreDB gestoreDB;
}
