package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.google.gson.Gson;

public class TaskRegJson extends ServerResource {

	@Get
	public String getTasks() throws ParseException, InvalidKeyException {
		Gson gson = new Gson();
		return gson.toJson(g.readFreeTasksTitle(), ArrayList.class);
	}

	@Post
	public String addTask(String payload) throws ParseException, InvalidUsernameException {

		Gson gson = new Gson();

		Task t = gson.fromJson(payload, Task.class);

		Date d = new Date();
		Calendar data = Calendar.getInstance();
		data.setTime(d);
		data.add(Calendar.DATE, -1);

		if (t.getDate().before(data.getTime())) {

			return gson.toJson("The deadline date is earlier than today", String.class);
		}

		if (t.getTitle().substring(0, 1).equalsIgnoreCase(" ")) {
			System.out.println(t.getTitle().substring(0, 0));
			return gson.toJson("Re-enter the title", String.class);
		}

		
		g.insertTask(t);

		return gson.toJson("Task added: " + t.getTitle(), String.class);

	}

	@Put
	public String updateTask(String payload) throws ParseException, FileNotFoundException, InvalidUsernameException {
		Gson gson = new Gson();

		Task n = gson.fromJson(payload, Task.class);

		g.updateTask(n);

		return gson.toJson("Task modified: " + n.getTitle(), String.class);

	}

	private GestoreDB g = GestoreDB.getInstance();

}
