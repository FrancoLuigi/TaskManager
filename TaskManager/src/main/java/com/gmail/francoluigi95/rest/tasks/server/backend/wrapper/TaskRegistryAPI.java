package com.gmail.francoluigi95.rest.tasks.server.backend.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.server.backend.TaskRegistry;

public class TaskRegistryAPI {

	public static synchronized TaskRegistryAPI instance() {
		if (instance == null)
			instance = new TaskRegistryAPI();
		return instance;
	}

	public synchronized Task get(String title) throws InvalidKeyException {
		return nr.get(title);
	}

	public synchronized ArrayList<String> getFreeTasks() throws InvalidKeyException {
		return nr.getFreeTasks();
	}

	public synchronized Set<String> titles() {
		return nr.titles();
	}


	public synchronized void add(Task n) throws InvalidKeyException {
		nr.add(n);
		commit();
	}

	public synchronized void update(Task n) {
		nr.update(n);
		commit();
	}

	public synchronized void remove(String title) throws InvalidKeyException {
		nr.remove(title);
		commit();
	}

	protected TaskRegistryAPI() {
		nr = new TaskRegistry();
	}

	// Imposto le informazioni di storage degli utenti

	public void setStorageFiles(String rootDirForStorageFile, String baseStorageFile) {
		this.rootDirForStorageFile = rootDirForStorageFile;
		this.baseStorageFile = baseStorageFile;
		System.err.println("Users Storage Directory: " + this.rootDirForStorageFile);
		System.err.println("Users Storage Base File: " + this.baseStorageFile);
	}

	// Costruisco l'estensione del file in base ai file già presenti all'interno
	// della cartella

	protected int buildStorageFileExtension() {
		final File folder = new File(rootDirForStorageFile);
		int c;
		int max = -1;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().substring(0, baseStorageFile.length()).equalsIgnoreCase(baseStorageFile)) {
				try {
					c = Integer.parseInt(fileEntry.getName().substring(baseStorageFile.length() + 1));
				} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
					c = -1;
				}
				if (c > max)
					max = c;
			}
		}
		return max;
	}

	// Effettuo il salvataggio (commit) delle modifiche su file

	public synchronized void commit() {
		int extension = buildStorageFileExtension();
		String fileName = rootDirForStorageFile + baseStorageFile + "." + (extension + 1);
		System.err.println("Commit storage to: " + fileName);
		try {
			nr.save(fileName);
		} catch (IOException e) {
			System.err.println("Commit filed " + e.getMessage() + " " + e.getCause());
		}
	}

	// Effettuo il recupero delle informazioni degli utenti (restore)

	public synchronized void restore() {
		int extension = buildStorageFileExtension();
		if (extension == -1) {
			System.err.println("No data to load - starting a new registry");
		} else {
			String fileName = rootDirForStorageFile + baseStorageFile + "." + extension;
			System.err.println("Restore storage from: " + fileName);
			try {
				nr.load(fileName);
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Restore filed - starting a new registry ");
				nr = new TaskRegistry();
			}
		}
	}

	private static TaskRegistryAPI instance;
	private TaskRegistry nr;
	private String rootDirForStorageFile;
	private String baseStorageFile;
}
