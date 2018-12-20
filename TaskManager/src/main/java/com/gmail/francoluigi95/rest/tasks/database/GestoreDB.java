package com.gmail.francoluigi95.rest.tasks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.commons.User1;

public class GestoreDB {
	public static GestoreDB getInstance() {
		if (singleGestoreDB == null) {
			singleGestoreDB = new GestoreDB();
		}
		return singleGestoreDB;
	}

	public boolean connectDB(String host, String port, String user, String pass) {

		String url = "jdbc:mysql://" + host + ":" + port + "/sys?useSSL=false";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
			System.err.println("Connection database established");
		} catch (SQLException e) {
			this.connection = null;
			System.err.println("Connection database not established");
			return false;
		}
		return true;
	}

	public void createDatabase() {
		try {
			Statement myStmt = connection.createStatement();

			String sql = "CREATE DATABASE IF NOT EXISTS TASKMANAGER DEFAULT CHARSET=utf8; ";

			myStmt.executeUpdate(sql);

			System.err.println("Database created");
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public void dropDatabase() {
		try {
			Statement myStmt = connection.createStatement();

			String sql = "DROP DATABASE IF EXISTS TASKMANAGER; ";

			myStmt.executeUpdate(sql);

			System.err.println("Database dropped");
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}
	
	public void createTableUsers() {
		try {

			Statement myStmt = connection.createStatement();
			String sql1 = "USE TASKMANAGER;";

			String sql3 = "CREATE TABLE IF NOT EXISTS users (" + "  username varchar(50) NOT NULL, "
					+ "  password char(50) NOT NULL, " +

					"  PRIMARY KEY (username)" +

					") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

			myStmt.executeUpdate(sql1);

			myStmt.executeUpdate(sql3);

			System.err.println("Table User created");
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public void createTableTasks() {
		try {

			Statement myStmt = connection.createStatement();
			String sql1 = "USE TASKMANAGER;";

			String sql3 = "CREATE TABLE IF NOT EXISTS tasks (" + "  titolo varchar(50) NOT NULL, "
					+ "  descrizione  varchar(50) NOT NULL, " + "  data DATETIME NOT NULL,"
					+ "  stato  varchar(50) NOT NULL, " + " user varchar(50) NOT NULL," +

					"  PRIMARY KEY (titolo)" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

			myStmt.executeUpdate(sql1);

			myStmt.executeUpdate(sql3);

			System.err.println("Table Tasks created");
		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public void insertUser(User1 u) {
		try {

			Statement myStmt = connection.createStatement();
			String sql1 = "USE TASKMANAGER;";

			String sql = "insert ignore into users " + " (username, password)" + " values ('" + u.getIdentifier()
					+ "', '" + String.valueOf(u.getSecret()) + "')";

			
			myStmt.executeUpdate(sql1);
			myStmt.executeUpdate(sql);

		}

		catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public void insertTask(Task t) {
		try {
			Statement myStmt = connection.createStatement();

			SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

			String sql = "insert ignore into tasks " + " (titolo, descrizione, data, stato, user)" + " values ('"
					+ t.getTitle() + "', '" + t.getText() + "', '" + sd.format(t.getDate()) + "', '" + t.getState()
					+ "', '" + t.getUser() + "')";

			myStmt.executeUpdate(sql);

		}

		catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	public Task readTask(String titolo) {
		Task t = null;
		SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		try {

			Statement myStmt = connection.createStatement();

			ResultSet myRs = myStmt.executeQuery("select * from tasks as t WHERE t.titolo='" + titolo + "';");

			if (myRs.next()) {
				t = new Task(myRs.getString("titolo"), myRs.getString("descrizione"), sd.parse(myRs.getString("data")),
						myRs.getString("stato"));
			}
			t.setUser(myRs.getString("user"));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return t;
	}

	public String readPassword(String username) {
		String p = null;

		try {

			Statement myStmt = connection.createStatement();

			ResultSet myRs = myStmt.executeQuery("select * from users as u WHERE u.username='" + username + "';");

			if (myRs.next()) {
				p = myRs.getString("password");

			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return p;
	}

	public ArrayList<String> readFreeTasksTitle() {
		Task t = null;
		ArrayList<String> tasks = new ArrayList<String>();
		SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		try {

			Statement myStmt = connection.createStatement();

			ResultSet myRs = myStmt.executeQuery("select * from tasks;");

			while (myRs.next()) {
				t = new Task(myRs.getString("titolo"), myRs.getString("descrizione"), sd.parse(myRs.getString("data")),
						myRs.getString("stato"));

				if (t.getState().equalsIgnoreCase("NoResponsabile")) {
					tasks.add(t.getTitle());

				}

			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return tasks;
	}

	public ArrayList<String> readAllTasksOfUser(String user) {
		Task t = null;
		ArrayList<String> tasks = new ArrayList<String>();
		SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		try {

			Statement myStmt = connection.createStatement();

			ResultSet myRs = myStmt.executeQuery("select * from tasks;");

			while (myRs.next()) {
				t = new Task(myRs.getString("titolo"), myRs.getString("descrizione"), sd.parse(myRs.getString("data")),
						myRs.getString("stato"));
				t.setUser(myRs.getString("user"));
				if (t.getUser().equalsIgnoreCase(user)) {
					tasks.add(t.toString());

				}

			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return tasks;
	}

	public boolean deleteTask(String task) {
		boolean result = false;
		try {
			Statement myStmt = connection.createStatement();
			String sql = "delete ignore from tasks WHERE titolo='" + task + "';";

			myStmt.executeUpdate(sql);
			result = true;

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return result;
	}

	public void updateTask(Task task) {
		SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		try {
			Statement myStmt = connection.createStatement();
			String sql = "update tasks set descrizione = '" + task.getText() + "', data = '" + sd.format(task.getDate())
					+ "', stato = '" + task.getState() + "' WHERE titolo='" + task.getTitle() + "';";

			myStmt.executeUpdate(sql);

		} catch (Exception exc) {
			exc.printStackTrace();

		}
	}

	
	public boolean checkConnectionAndReconnect() {

		try {
			Statement myStmt = connection.createStatement();
			myStmt.executeQuery("select 1");
		} catch (Exception exc) {

			if (connectDB(DBSettings.host, DBSettings.port, DBSettings.user, DBSettings.pass))
				return true;
			else
				return false;

		}

		return true;
	}

	public boolean isConnected() {

		try {
			Statement myStmt = connection.createStatement();
			myStmt.executeQuery("select 1");
		} catch (Exception exc) {
			return false;
		}

		return true;
	}

	public Connection connection;
	private static GestoreDB singleGestoreDB;

}
