package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;
import static ch.epfl.rigel.coordinates.EquatorialCoordinates.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlanetTest {

	@Test
	void PlanetWellImplemented() {
		Planet planet=new Planet("Venus", ofDeg(23, 45), (float) 23.5, (float) -34.5);
		
		assertEquals(planet.name(), "Venus");
		assertEquals(planet.equatorialPos().toString(), ofDeg(23, 45).toString());
		assertEquals(planet.angularSize(), 23.5);
		assertEquals(planet.magnitude(), -34.5);
	}
	
	@Test
	void CelestialObjectThrowsIAE() {
		//Randomization to add
		
		assertThrows(IllegalArgumentException.class, () -> {
			Planet planet=new Planet("Terre", ofDeg(23, 45), (float) -23.5, (float) -34.5);
		});	
	}
	
	@Test
	void CelestialObjectThrowsNPEString() {
		
		assertThrows(NullPointerException.class, () -> {
			Planet planet=new Planet(null, ofDeg(23, 45), (float) 23.5, (float) -34.5);
		});
	}
	
	@Test
	void CelestialObjectThrowsNPEEquatorial() {
		
		assertThrows(NullPointerException.class, () -> {
			Planet planet=new Planet("Terre", null, (float) 23.5, (float) -34.5);
		});
	}
}
