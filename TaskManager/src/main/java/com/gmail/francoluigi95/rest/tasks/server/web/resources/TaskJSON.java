package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.text.ParseException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.ErrorCodes;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidKeyException;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.Task;
import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.TaskRegistryAPI;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.UserRegistryAPI;
import com.google.gson.Gson;

public class TaskJSON extends ServerResource {

	@Get
	public String getTask() throws ParseException {
		Gson gson = new Gson();
		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();
		try {
			Task n = nrapi.get(getAttribute("title"));
			return gson.toJson(n, Task.class);
		} catch (InvalidKeyException e) {
			Status s = new Status(ErrorCodes.INVALID_KEY_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidKeyException.class);
		}
	}

	@Delete
	public String deleteTask() throws InvalidUsernameException {
		Gson gson = new Gson();
		// ottengo una istanza del registro
		TaskRegistryAPI nrapi = TaskRegistryAPI.instance();
		UserRegistryAPI urapi = UserRegistryAPI.instance();

		try {
			Task c = nrapi.get(getAttribute("title"));
			nrapi.remove(c.getTitle());
			User1 u=urapi.get(c.getUser());
			u.removeTask(c);
			
			return gson.toJson("Task removed");
			

		} catch (InvalidKeyException e) {
			Status s = new Status(ErrorCodes.INVALID_KEY_CODE);
			setStatus(s);
			return gson.toJson(e, InvalidKeyException.class);
		}
	}

}
