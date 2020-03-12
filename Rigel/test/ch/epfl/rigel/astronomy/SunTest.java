package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.coordinates.EquatorialCoordinates.ofDeg;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticCoordinates;

public class SunTest {
	
	@Test
	void SunWellImplemented() {
		//Randomize the test using the TestRandomizer class
		Sun sun=new Sun(EclipticCoordinates.ofDeg(34, 56), ofDeg(23, 45), (float) 23.5, (float) -34.5);
		
		assertEquals(sun.name(), "Soleil");
		assertEquals(ofDeg(23, 45).toString(), sun.equatorialPos().toString());
		assertEquals(sun.angularSize(), 23.5);
		assertEquals(-26.7f, sun.magnitude());
		assertEquals(EclipticCoordinates.ofDeg(34, 56).toString(), sun.eclipticPos().toString());
		assertEquals(-34.5, sun.meanAnomaly());
	}
	
	@Test
	void SunThrowsNPE() {
		assertThrows(NullPointerException.class, () -> {
			Sun sun=new Sun(null, ofDeg(23, 45), (float) 23.5, (float) -34.5);
		});
	}

}
