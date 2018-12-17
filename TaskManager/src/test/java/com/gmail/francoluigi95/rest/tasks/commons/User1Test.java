package com.gmail.francoluigi95.rest.tasks.commons;

import java.io.Serializable;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class User1Test implements Serializable {

	private User1 u;
	private Task t = new Task("a", "b", new Date(), "free");

	@Before
	public void setUp() throws Exception {
		char[] pass = { '1', '2', '3', '4' };
		u = new User1("Ciccio", pass);
		u.getTasks().put(t.getTitle(), t);
		Assert.assertNotNull("Create user", u);

	}

	@Test
	public void getIdentifier() {

		Assert.assertEquals("ErrorTest getIdentifier ", u.getIdentifier(), "Ciccio");

	}

	@Test
	public void getSecret() {
		char[] pass = { '1', '2', '3', '4' };
		Assert.assertEquals("ErrorTest getSecret ", String.valueOf(u.getSecret()), String.valueOf(pass));

	}

	@Test
	public void addTask() {

		int size = u.getTasks().size();

		Task t1 = new Task("c", "d", new Date(), "free");
		u.addTask(t1);

		Assert.assertNotSame(u.getTasks().size(), size);// ("ErrorTest addTask ", size, u.getTasks().size());

	}

	@Test
	public void removeTask() {

		int size = u.getTasks().size();

		u.removeTask(t);

		Assert.assertNotSame(u.getTasks().size(), size);// ("ErrorTest addTask ", size, u.getTasks().size());

	}

	@Test
	public void testToString() {

		String test = "User: " + u.getIdentifier() + ", " + String.valueOf(u.getSecret()) + " ";

		Assert.assertEquals("ErrorTest ToString", u.toString(), test);

	}

	private static final long serialVersionUID = 1L;

}
