package com.gmail.francoluigi95.rest.tasks.server.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;

public class TaskRegistry {

	public TaskRegistry() {
		reg = new HashMap<String, Task>();

	}



	public Task get(String title) throws InvalidKeyException {
		Task Task = reg.get(title);
		if (Task != null)
			return Task;
		throw new InvalidKeyException("Chiave non valida: " + title);
	}

	// GET di tutte le Task

	public Set<String> titles() {
		return reg.keySet();
	}

	public ArrayList<String> getFreeTasks() {

		ArrayList<String> tasks = new ArrayList<String>();
		Set<String> keySet = reg.keySet();
		for (String key : keySet) {
			Task t = reg.get(key);

			if (t.getState().equalsIgnoreCase("NoResponsabile"))
				tasks.add(t.getTitle());
		}
		
		
		return tasks;

	}

	

	public void add(Task t) throws InvalidKeyException {
		if (reg.containsKey(t.getTitle()))
			throw new InvalidKeyException("Key duplicata: " + t.getTitle());
		reg.put(t.getTitle(), t);

	}

	// UPDATE di una entry nella mappa

	public void update(Task Task) {
		reg.put(Task.getTitle(), Task);
	}

	// DELETE di una entry nella mappa

	public void remove(String title) throws InvalidKeyException {
		if (!reg.containsKey(title))
			throw new InvalidKeyException("Chiave non valida: " + title);
		reg.remove(title);
	}

	// CONTAINS di una entry, data la key

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

	@SuppressWarnings("unchecked")
	public void load(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		reg = (HashMap<String, Task>) in.readObject();
		in.close();
		fileIn.close();
	}


	private HashMap<String, Task> reg;

}
