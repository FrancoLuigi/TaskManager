package com.gmail.francoluigi95.rest.tasks.commons;

import java.io.Serializable;
import java.util.HashMap;

public class User1 implements Serializable {

	private static final long serialVersionUID = 1L;

	// Costruttore oggetto User
	public User1(String identifier, char[] secret) {
		this.identifier = identifier;
		this.secret = secret;
		tasks = new HashMap<String,Task>();

	}

	public String getIdentifier() {
		return identifier;
	}

	public char[] getSecret() {
		return secret;
	}

	public HashMap<String,Task> getTasks() {
		return tasks;
	}

	public void setTasks(HashMap<String,Task> tasks) {
		this.tasks = tasks;
	}

	public void addTask(Task t) {
		tasks.put(t.getTitle(),t);
	}
	

	public void removeTask(Task t) {
		tasks.remove(t.getTitle());
	}
	public String toString() {

		return "User: " + identifier + ", " + secret.toString() + " ";
	}

	private String identifier;
	private char[] secret;
	private HashMap<String,Task> tasks;

}
