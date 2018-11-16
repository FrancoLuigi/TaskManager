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

import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.TaskRegistryAPI;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.UserRegistryAPI;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskJSON;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.TaskRegJson;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserLogJSON;
import com.gmail.francoluigi95.rest.tasks.server.web.resources.UserRegJSON;
import com.google.gson.Gson;

public class TaskRegistryWebApp extends Application {


	/* Creo la classe relative alle impostazioni del server */

	private class Settings {
		public int port; // Porta utilizzata dal server sulla quale fornisce i propri servizi
		public String storage_base_dir; // Directory per lo storage delle note
		public String storage_base_file; // File per lo storage delle note
		public String users_storage_base_dir; // Directory per lo storage degli utenti
		public String users_storage_base_file; // File per lo storage degli utenti
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
			Scanner scanner = new Scanner(new File("settings.json"));
			settings = gson.fromJson(scanner.nextLine(), Settings.class);
			scanner.close();
			System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}

		rootDirForWebStaticFiles = "file:///" + System.getProperty("user.dir") + "//" + settings.web_base_dir;
		System.err.println("Web Directory: " + rootDirForWebStaticFiles);

		// Richiamo le singole istanze (sono singleton) delle API per i registri di
		// oggetti e utenti ed effettuo il restore

		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();
		nrapi.setStorageFiles(System.getProperty("user.dir") + "//" + settings.storage_base_dir + "//",
				settings.storage_base_file); // Imposto i file di storage
		nrapi.restore();

		UserRegistryAPI urapi = UserRegistryAPI.instance();
		urapi.setStorageFiles(System.getProperty("user.dir") + "//" + settings.users_storage_base_dir + "//",
				settings.users_storage_base_file); // Imposto i file di storage
	
		urapi.restore();

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
}
