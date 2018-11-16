package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.ErrorCodes;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.TaskRegistryAPI;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.UserRegistryAPI;
import com.google.gson.Gson;

public class TaskRegJson extends ServerResource {

	@Get
	public String getTasks() throws ParseException, InvalidKeyException {
		Gson gson = new Gson();
		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();
		return gson.toJson(nrapi.getFreeTasks(), ArrayList.class);
	}

	@Post
	public String addTask(String payload) throws ParseException, InvalidUsernameException {
		Gson gson = new Gson();
		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();

		Task t = gson.fromJson(payload, Task.class);
		UserRegistryAPI urapi = UserRegistryAPI.instance();

		User1 u = urapi.get(t.getUser());
		Date d = new Date();
		Calendar data = Calendar.getInstance();
		data.setTime(d);
		data.add(Calendar.DATE, -1);

		if (t.getDate().before(data.getTime())) {

			return gson.toJson("The deadline date is earlier than today", String.class);
		}
		
		if(t.getTitle().substring(0, 1).equalsIgnoreCase(" ")) {
			System.out.println(t.getTitle().substring(0, 0));
			return gson.toJson("Re-enter the title", String.class);
		}

		try {
			nrapi.add(t);
			urapi.addTask(u, t);

			return gson.toJson("Task added: " + t.getTitle(), String.class);
		} catch (InvalidKeyException e) {
			Status s = new Status(ErrorCodes.INVALID_KEY_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidKeyException.class);
		}
	}

	@Put
	public String updateTask(String payload) throws ParseException, FileNotFoundException, InvalidUsernameException {
		Gson gson = new Gson();
		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();
		UserRegistryAPI urapi=UserRegistryAPI.instance();
		Task n = gson.fromJson(payload, Task.class);
		
		nrapi.update(n);
		User1 u=urapi.get(n.getUser());
		
		u.addTask(n);
		
		
		urapi.update(u);
		
		return gson.toJson("Task modified: " + n.getTitle(), String.class);

	}

	
}
