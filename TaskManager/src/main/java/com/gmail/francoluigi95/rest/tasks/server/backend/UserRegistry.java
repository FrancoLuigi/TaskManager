package com.gmail.francoluigi95.rest.tasks.server.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.commons.User1;

public class UserRegistry {

	public UserRegistry() {

		reg = new HashMap<String, User1>();
	}

	
	public Map<String, User1> getUsers() {
		return reg;
	}

	public User1 get(String username) throws InvalidUsernameException {
		User1 u = reg.get(username);
		if (u != null)
			return u;
		throw new InvalidUsernameException("Username non valido: " + username);

	}

	public ArrayList<String> getAllTasks(String username) {
		User1 u = reg.get(username);
		ArrayList<String> tasks = new ArrayList<String>();

		if (u.getTasks() == null) {

			return null;
		}

		else {

			for (Task s : u.getTasks().values()) {

				tasks.add(s.toString());
			}
		}

		return tasks;

	}

	public void addTask(User1 u, Task t) {

		if (u.getTasks() != null)
			u.addTask(t);
		else {
			HashMap<String,Task> a = new HashMap<String,Task>();
			a.put(t.getTitle(), t);
			u.setTasks(a);
		}

		reg.put(u.getIdentifier(), u);

	}

	public void add(User1 u) throws InvalidUsernameException {

		if (reg.containsKey(u.getIdentifier()))
			throw new InvalidUsernameException("Username Duplicato:" + u.getIdentifier());
		reg.put(u.getIdentifier(), u);
	}

	public void update(User1 u) {
		reg.put(u.getIdentifier(), u);
	}

	public void remove(String username) throws InvalidUsernameException {

		if (!reg.containsKey(username))
			throw new InvalidUsernameException("Username non valido: " + username);
		reg.remove(username);
	}

	public boolean contains(String key) {
		return reg.containsKey(key);
	}

	public void save(String fileOutName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileOutName);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(reg);
		out.close();
		fileOut.close();
	}

	// Caricamento della lista utenti da file

	@SuppressWarnings("unchecked")
	public void load(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		reg = (HashMap<String, User1>) in.readObject();
		in.close();
		fileIn.close();
	}

	private HashMap<String, User1> reg;
}
