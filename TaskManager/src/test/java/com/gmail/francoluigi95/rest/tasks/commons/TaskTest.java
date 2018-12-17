package com.gmail.francoluigi95.rest.tasks.commons;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskTest implements Serializable {

	Task t;

	@Before
	public void setUp() throws Exception {
		GregorianCalendar gregorianCalendar1 = new GregorianCalendar(2017, GregorianCalendar.SEPTEMBER, 27);

		Date data = gregorianCalendar1.getTime();
		t = new Task("TitleTest", "TextTest", data, "StateTest");
		Assert.assertNotNull("Create Task", t);

	}

	@Test
	public void getTitle() {
		Assert.assertEquals("ErrorTest getTitle", t.getTitle(), "TitleTest");

	}

	@Test
	public void getText() {

		Assert.assertEquals("ErrorTest getText", t.getText(), "TextTest");
	}

	@Test
	public void getDate() {
		GregorianCalendar gregorianCalendar1 = new GregorianCalendar(2018, GregorianCalendar.DECEMBER, 17);
		Date data = gregorianCalendar1.getTime();

		Assert.assertEquals("ErrorTest getDate", t.getDate(), data);
	}

	@Test
	public void setTitle() {
		String title = t.getTitle();
		t.setTitle("Title modified");

		Assert.assertNotSame("ErrorTest setTitle", t.getTitle(), title);
	}

	@Test
	public void setState() {
		String state = t.getState();
		t.setState("State modified");

		Assert.assertNotSame("ErrorTest setTitle", t.getState(), state);
	}

	@Test
	public void setText() {
		String text = t.getText();
		t.setText("Text modified");
		Assert.assertNotSame(" ErrorTest setTitle", t.getText(), text);
	}

	@Test
	public void setDate() {
		Date d = t.getDate();
		GregorianCalendar gregorianCalendar1 = new GregorianCalendar(2017, GregorianCalendar.NOVEMBER, 12);
		Date data1 = gregorianCalendar1.getTime();
		t.setDate(data1);
		Assert.assertNotSame(" ErrorTest setDate", t.getDate(), d);

	}

	@Test
	public void testToString() {

		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyy");
		String test = "Title: " + t.getTitle() + ", " + "text: " + t.getText() + ", " + "date: "
				+ sd.format(t.getDate()) + ", " + "state: " + t.getState() + ", " + "user: " + t.getUser() + ";";

		Assert.assertEquals("ErrorTest ToString", t.toString(), test);

	}

	private static final long serialVersionUID = 1L;

}
