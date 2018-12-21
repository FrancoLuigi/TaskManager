package com.gmail.francoluigi95.rest.tasks.commons;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


//Test suite che racchiude tutti i test cases delle classi base

@RunWith(Suite.class)
@SuiteClasses({ TaskTest.class, User1Test.class })
public class AllCommonsTests {

}
