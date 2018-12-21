package com.gmail.francoluigi95.test.tasks.server.web.resources;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//  Test suite che racchiude tutti i test cases json

@RunWith(Suite.class)
@SuiteClasses({ TaskRegJsonTest.class, UserLogJSONTest.class, UserRegJSONTest.class })
public class AllJsonTests {

}
