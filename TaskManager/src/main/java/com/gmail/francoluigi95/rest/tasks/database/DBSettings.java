package com.gmail.francoluigi95.rest.tasks.database;

import java.io.*;

public class DBSettings {

	public static void readSettingsFromFile() {

		FileReader f = null;
		try {
			f = new FileReader("files/db_config.txt");
		} catch (FileNotFoundException e) {
			System.err.println("File di configurazione database 'db_config.txt' non trovato!");
			System.exit(0);

		}
		b = new BufferedReader(f);

		try {
			host = b.readLine();
			port = b.readLine();
			user = b.readLine();
			pass = b.readLine();
			b.close();
		} catch (IOException e) {
			System.err.println("Errore nella lettura del file di configurazione DB!");
		}

	}

	public static void updateSettingsInFile(String host, String port, String user, String pass) {

		try {
			w = new FileWriter("files/db_config.txt");
		} catch (IOException e) {
			System.err.println("File di configurazione database 'db_config.txt' non trovato!");
		}

		BufferedWriter b;
		b = new BufferedWriter(w);

		try {
			b.write(host + "\n" + port + "\n" + user + "\n" + pass);
			b.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

		readSettingsFromFile();

	}

	private static BufferedReader b;
	private static FileWriter w;
	public static String host;
	public static String port;
	public static String user;
	public static String pass;

}
