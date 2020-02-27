package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
 

import org.junit.jupiter.api.Test;

import static ch.epfl.rigel.math.Angle.*;

public class EclipticCoordinatesTest {

	@Test 
	void ofFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			EclipticCoordinates ecli=EclipticCoordinates.of(TAU, 0.25);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			EclipticCoordinates ecli=EclipticCoordinates.of(0.25, Math.PI+1);
		});
	}
	
	@Test 
	void gettersWellDefined() {
		EclipticCoordinates ecli1=of(ofDeg(29), ofDeg(57));
		EclipticCoordinates ecli2=of(ofDeg(45), ofDeg(87));
		
		assertEquals(ofDeg(29), ecli1.lon());
		assertEquals(ofDeg(57), ecli1.lat());
		assertEquals(29, ecli1.lonDeg(), 1e-5);
		assertEquals(57, ecli1.latDeg(), 1e-5);
		
		assertEquals(ofDeg(45), ecli2.lon());
		assertEquals(ofDeg(87), ecli2.lat());
		assertEquals(45, ecli2.lonDeg(), 1e-5);
		assertEquals(87, ecli2.latDeg(), 1e-5);
	}
	
	@Test
	void toStringWorksOnKnownEclipticCoordinates() {
		EclipticCoordinates ecli=EclipticCoordinates.of(ofDeg(22.5000), ofDeg(18.0000));
		assertEquals("(λ=22.5000°, β=18.0000°)", ecli.toString());
	}
}
