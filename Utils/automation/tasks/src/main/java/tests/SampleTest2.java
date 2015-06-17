package tests;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by asher.saban on 2/25/2015.
 */
public class SampleTest2 {

	@BeforeClass
	public void initializeTest() throws Exception {
		Reporter.log("BEFORE 2-----------------------", true);
		System.out.println("BEFORE -----------------------");
	}

	@Test
	public void compareFiles() {
		Reporter.log("TEST 2-----------------------",true);
		System.out.println("TEST 2 -----------------------");
		Assert.assertTrue(true);

	}

	@AfterClass
	public void clearResources() {
		Reporter.log("AFTER 2 -----------------------",true);
		System.out.println("AFTER 2-----------------------");

	}
}
