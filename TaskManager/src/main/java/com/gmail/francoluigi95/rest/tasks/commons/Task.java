package com.gmail.francoluigi95.rest.tasks.commons;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	public Task(String title, String text, Date date, String state) {
		this.title = title;
		this.text = text;
		this.date = date;
		this.state = state;
		user = null;

	}

	public String getUser() {
		return user;
	}

	public void setString(String user) {
		this.user = user;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public Date getDate() {
		return date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String toString() {

		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyy");

		return "Titolo: " + title + ", " + "testo: " + text + ", " + "data: " + sd.format(date) + ", " + "stato: "
				+ state + ", " + "user: " + user + ";";
		// return title+" ,"+text+" ,"+sd.format(date)+" ,"+state+" ,"+user+" -";
	}

	private String title, text;
	private Date date;
	private String state;
	private String user;

}
