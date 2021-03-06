package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.text.ParseException;
import java.util.ArrayList;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.ErrorCodes;
import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.google.gson.Gson;

public class UserRegJSON extends ServerResource {

	@Post
	public String addUser(String payload) throws ParseException {
		Gson gson = new Gson();

		User1 u = gson.fromJson(payload, User1.class);
		try {
			g.insertUser(u);
			return gson.toJson("User added: " + u.getIdentifier(), String.class);
		}
		catch (InvalidUsernameException e){    		
    		Status s = new Status(ErrorCodes.INVALID_KEY_CODE);
    		setStatus(s);
    		return gson.toJson(e, InvalidUsernameException.class);
    	}

	}

	@Get
	public String getTask() {
		Gson gson = new Gson();
		return gson.toJson(g.readAllTasksOfUser(getAttribute("username")), ArrayList.class);

	}

	private GestoreDB g = GestoreDB.getInstance();
}
