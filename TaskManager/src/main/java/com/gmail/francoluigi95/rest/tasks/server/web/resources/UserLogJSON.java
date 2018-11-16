package com.gmail.francoluigi95.rest.tasks.server.web.resources;

import java.util.StringTokenizer;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.gmail.francoluigi95.rest.tasks.commons.InvalidUsernameException;
import com.gmail.francoluigi95.rest.tasks.commons.User1;
import com.gmail.francoluigi95.rest.tasks.server.backend.wrapper.UserRegistryAPI;
import com.google.gson.Gson;

public class UserLogJSON extends ServerResource {

	
	@Post
	public String checkUser(String payload) {
		Gson gson = new Gson();
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		String response=gson.fromJson(payload, String.class);
		StringTokenizer st = new StringTokenizer(response,";");
		String username = st.nextToken();
		String password = st.nextToken();
		User1 u=null;
		try {
			u = urapi.get(username);
		} catch (InvalidUsernameException e) {
			return gson.toJson(false, Boolean.class);
		}
		if(u!=null && u.getIdentifier().equalsIgnoreCase(username) && String.copyValueOf(u.getSecret()).equalsIgnoreCase(password))
			return gson.toJson(true, Boolean.class);
		 return gson.toJson(false, Boolean.class);


	}
}
