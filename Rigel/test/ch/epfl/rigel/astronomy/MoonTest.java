package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.coordinates.EquatorialCoordinates.ofDeg;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MoonTest {

	@Test
	void MoonWellImplemented() {
		//Randomize the test using the TestRandomizer 
		Moon moon=new Moon(ofDeg(23, 45), (float) 23.5, (float) -34.5, (float) 0.1);
		
		
		assertEquals("Lune" ,moon.name());
		assertEquals(ofDeg(23, 45).toString() ,moon.equatorialPos().toString());
		assertEquals(23.5, moon.angularSize());
		assertEquals(-34.5, moon.magnitude());
	}
	
	
	@Test
	void MoonThrowsIAE() {
		//Randomize the test using the TestRandomizer class
		
		assertThrows(IllegalArgumentException.class, () -> {
			Moon moon=new Moon(ofDeg(23, 45), (float) 23.5, (float) -34.5, (float) 78);
		});
	}
	
	@Test
	void infoWorksOnKnownMoonPositions() {
		//Randomize the test using the TestRandomizer class
		
		Moon moon=new Moon(ofDeg(23, 45), (float) 23.5, (float) -34.5, (float) 0.375);
		Moon moon1=new Moon(ofDeg(23, 45), (float) 23.5, (float) -34.5, (float) 0.9874);
		Moon moon2=new Moon(ofDeg(23, 45), (float) 23.5, (float) -34.5, (float) 0.4569);
		
		
		assertEquals("Lune (37.5%)", moon.info());
		assertEquals("Lune (98.7%)", moon1.info());
		assertEquals("Lune (45.7%)", moon2.info());
	}
	
	//Moon well implemented verification through CelestialObject getters
	//Constructor throws exception if phase is not in interval [0, 1]
	//infoWorksOnKnownMoonPosition()
	
}
