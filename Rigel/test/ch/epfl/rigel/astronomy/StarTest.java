package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class StarTest {

	@Test
	void StarFailsWithInvalidInputs() {
		//Randomization
		assertThrows(IllegalArgumentException.class, () -> {
			Star s=new Star(-6, "Rigel", EquatorialCoordinates.ofDeg(34, 34), -7, (float) 2.5);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			Star s1=new Star(23, "Rigel", EquatorialCoordinates.ofDeg(34, 34), -7f, -90f);
		});
	}
	
	//Randomization
	@Test
	void gettersWellDefined() {
		Star s=new Star(78, "Rigel", EquatorialCoordinates.ofDeg(34, 34), -7, (float) 2.5);
		
		assertEquals(78, s.hipparcosId());
		assertEquals(2, s.colorTemperature());
	}
}
