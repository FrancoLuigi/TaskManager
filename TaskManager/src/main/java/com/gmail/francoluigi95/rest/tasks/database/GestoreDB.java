package com.gmail.francoluigi95.rest.tasks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
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

	public boolean closeConnection() {
		try {
			this.connection.close();
			System.err.println("Connection closed!");
			return true;
		} catch (SQLException e) {
			System.err.println("Connection cannot be closed!");
			return false;
		}
	}

	public boolean createDatabase() {
		try {
			Statement myStmt = connection.createStatement();

			String sql = "CREATE DATABASE IF NOT EXISTS TASKMANAGER DEFAULT CHARSET=utf8; ";

			myStmt.executeUpdate(sql);

			System.err.println("Database created");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	public boolean dropDatabase() {
		try {
			Statement myStmt = connection.createStatement();

			String sql = "DROP DATABASE IF EXISTS TASKMANAGER; ";

			myStmt.executeUpdate(sql);

			System.err.println("Database dropped");
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	public boolean createTableUsers() {
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
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	public boolean createTableTasks() {
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
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	public void insertUser(User1 u) throws InvalidUsernameException {
		try {

			Statement myStmt = connection.createStatement();

			String sql1 = "USE TASKMANAGER;";
			myStmt.executeUpdate(sql1);
			
			ResultSet myRs = myStmt.executeQuery("select * from users as u WHERE u.username='" + u.getIdentifier() + "';");

			if (myRs.next()) {
				throw new InvalidUsernameException("Username duplicate:" + u.getIdentifier());
			}

		

			String sql = "insert ignore into users " + " (username, password)" + " values ('" + u.getIdentifier()
					+ "', '" + String.valueOf(u.getSecret()) + "')";

			myStmt.executeUpdate(sql1);
			myStmt.executeUpdate(sql);

		}

		catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public void insertTask(Task t) throws InvalidKeyException{
		try {
			Statement myStmt = connection.createStatement();
			String sql1 = "USE TASKMANAGER;";
			myStmt.executeUpdate(sql1);
			
			ResultSet myRs = myStmt.executeQuery("select * from tasks as t WHERE t.titolo='" + t.getTitle() + "';");

			if (myRs.next()) {
				throw new InvalidKeyException("Task duplicate:" + t.getTitle());
			}
			

			SimpleDateFormat sd = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

			String sql = "insert ignore into tasks " + " (titolo, descrizione, data, stato, user)" + " values ('"
					+ t.getTitle() + "', '" + t.getText() + "', '" + sd.format(t.getDate()) + "', '" + t.getState()
					+ "', '" + t.getUser() + "')";

			myStmt.executeUpdate(sql);

		}

		catch (SQLException exc) {
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
			return true;
		} catch (Exception exc) {
			return false;
		}
	}

	public Connection connection;
	private static GestoreDB singleGestoreDB;

}
