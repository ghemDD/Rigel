package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static ch.epfl.rigel.math.Angle.*;

public class EquatorialToHorizontalCoordinatesTest {

	
	@Test
	void conversionWork() {
		
		//Book test
		EquatorialCoordinates t=EquatorialCoordinates.of(0.0, ofDMS(23, 13, 10));
		HorizontalCoordinates hori=new EquatorialToHorizontalConversion(ofDeg(87.933333), ofDeg(52))
									.apply(t);
		
		assertEquals(ofDMS(283, 16, 15.70), hori.az(), 1E-7);
		assertEquals(ofDMS(19, 20, 3.64), hori.alt(), 1E-7);
	}
}
