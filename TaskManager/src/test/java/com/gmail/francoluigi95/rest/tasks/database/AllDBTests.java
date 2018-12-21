package com.gmail.francoluigi95.rest.tasks.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

// Test suite che racchiude tutti i test cases del database

@RunWith(Suite.class)
@SuiteClasses({ GestoreDBTest.class, GestoreDBTest2.class })
public class AllDBTests {

}
