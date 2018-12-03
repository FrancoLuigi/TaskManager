package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.util.StringTokenizer;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.database.GestoreDB;
import com.google.gson.Gson;

public class UserLogJSON extends ServerResource {

	@Post
	public String checkUser(String payload) {
		Gson gson = new Gson();
	
		String response = gson.fromJson(payload, String.class);
		StringTokenizer st = new StringTokenizer(response, ";");
		String username = st.nextToken();
		String password = st.nextToken();
		String passwordDB = g.readPassword(username);
		if (password.equals(passwordDB)) {
			return gson.toJson(true, Boolean.class);
		}

		return gson.toJson(false, Boolean.class);

	}

	private GestoreDB g = GestoreDB.getInstance();
}
