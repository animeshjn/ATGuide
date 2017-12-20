package edu.gmu.ttaconline.atcguide;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnitTest2 {

	UnitTest ft = new UnitTest();
	String s = "Paper (e.g legal pad)";
    			
	@Test
	public void test() {
		assertEquals(ft.trimATName(s),"Paper ");
	}

}
