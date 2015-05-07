package controllers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import classSchedule.ClassController;
import classSchedule.model.Major;
import classSchedule.model.User;
import classSchedule.persist.DatabaseProvider;
import classSchedule.persist.FakeDatabase;

public class ClassControllerTest {
	private ClassController control;
	
	private User aUser;
	
	@Before
	public void setUp() {
		FakeDatabase db = new FakeDatabase();
		DatabaseProvider.setInstance(db);
		control = new ClassController();
		
		aUser = db.findUser("Coover", "test3"); // part of the initial data
	}
	
	@Test
	public void testFindCourseByMajor() throws Exception {
		Major aMajor = control.findMajorByUser(aUser);
		assertNotNull(control.findCourseByMajor(aMajor));
	}
	
	@Test
	public void testFindMajorByUser() throws Exception {
		User not_a_user = new User();
		assertNull(control.findMajorByUser(not_a_user));
		assertNotNull(control.findMajorByUser(aUser));
	}

}
