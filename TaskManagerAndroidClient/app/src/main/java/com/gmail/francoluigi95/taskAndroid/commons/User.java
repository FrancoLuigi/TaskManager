package com.gmail.francoluigi95.taskAndroid.commons;

import java.util.ArrayList;

public class User {
	


	
	// Costruttore oggetto UserWrap
	
	public User(String identifier, char [] secret)
	{
		this.identifier = identifier;
		this.secret = secret;

		tasks=new ArrayList<String>();
		//this.role = role;
	}
	
	// Costruttore vuoto per persistenza (richiesto dalla libreria JACKSON e dall'ObjectMapper)
	public User(){};
	
	
	// GET dell'identifier
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	// GET della secret
	
	public char[] getSecret()
	{
		return secret;
	}
	
	
	
	private String identifier; 
	private char[] secret;
	private ArrayList<String>tasks;


}
