package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.text.ParseException;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.google.gson.Gson;

public class TaskJSON extends ServerResource {

	@Get
	public String getTask() throws ParseException {
		Gson gson = new Gson();

		Task t = g.readTask(getAttribute("title"));
		return gson.toJson(t, Task.class);

	}

	@Delete
	public String deleteTask() throws InvalidUsernameException {
		Gson gson = new Gson();
		// ottengo una istanza del registro
		g.deleteTask(getAttribute("title"));
		return gson.toJson("Task removed");

	}

	private GestoreDB g = GestoreDB.getInstance();
}
