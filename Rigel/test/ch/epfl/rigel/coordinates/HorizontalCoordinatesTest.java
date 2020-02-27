package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.coordinates.HorizontalCoordinates.ofDeg;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class HorizontalCoordinatesTest {
	//Precision of angular distance?
	//

	@Test 
	void ofFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			HorizontalCoordinates hor=HorizontalCoordinates.of(Angle.TAU, 0.25);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			HorizontalCoordinates hor=HorizontalCoordinates.of(0.25, Math.PI+1);
		});
	}

	@Test 
	void ofDegFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			HorizontalCoordinates hor=HorizontalCoordinates.ofDeg(432, 767);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			HorizontalCoordinates hor=HorizontalCoordinates.ofDeg(890, 990);
		});
	}

	@Test 
	void gettersWellDefined() {
		HorizontalCoordinates hor1=ofDeg(29, 57);
		HorizontalCoordinates hor2=ofDeg(45, 87);

		assertEquals(Angle.ofDeg(29), hor1.az());
		assertEquals(Angle.ofDeg(57), hor1.alt());
		assertEquals(29, hor1.azDeg(), 1e-5);
		assertEquals(57, hor1.altDeg(), 1e-5);

		assertEquals(Angle.ofDeg(45), hor2.az());
		assertEquals(Angle.ofDeg(87), hor2.alt());
		assertEquals(45, hor2.azDeg(), 1e-5);
		assertEquals(87, hor2.altDeg(), 1e-5);
	}

	@Test
	void azOctantNameWorksOnKnownHorizontalCoordinates() {
		assertEquals("NO", HorizontalCoordinates.ofDeg(335, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("N", HorizontalCoordinates.ofDeg(337.5, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("NE", HorizontalCoordinates.ofDeg(22.5, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("E", HorizontalCoordinates.ofDeg(90, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("SE", HorizontalCoordinates.ofDeg(136, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("S", HorizontalCoordinates.ofDeg(180, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("SO", HorizontalCoordinates.ofDeg(220, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("O", HorizontalCoordinates.ofDeg(270, 0)
				.azOctantName("N", "E", "S", "O"));
	}

	@Test
	void angularDistanceToWorksOnKnownLocations() {
		HorizontalCoordinates epfl=HorizontalCoordinates.ofDeg(6.5682, 46.5183);
		double dist1=epfl.angularDistanceTo(HorizontalCoordinates.ofDeg(8.5476, 47.3763));

		assertEquals(0.0279, dist1, 1E-4);
		
		HorizontalCoordinates paris=HorizontalCoordinates.ofDeg(2.3488, 48.8534);
		double dist2=paris.angularDistanceTo(epfl);

		assertEquals(Angle.ofDeg(3.6761), dist2, 1E-4);
		
		double ndist=epfl.angularDistanceTo(epfl);
		
		assertEquals(0.0, ndist);
		
		HorizontalCoordinates tunis=HorizontalCoordinates.ofDeg(10.1657, 36.8189);
		double dist3=paris.angularDistanceTo(tunis);
		
		assertEquals(Angle.ofDeg(13.3128), dist3, 1E-4);
	}

	@Test
	void toStringWorksOnKnownHorizontalCoordinates() {
		HorizontalCoordinates hor1=HorizontalCoordinates.ofDeg(350, 7.2);
		assertEquals("(az=350.0000°, alt=7.2000°)", hor1.toString());
		
		HorizontalCoordinates hor2=HorizontalCoordinates.ofDeg(230, 3.22233);
		assertEquals("(az=230.0000°, alt=3.2223°)", hor2.toString());
		
		HorizontalCoordinates hor3=HorizontalCoordinates.ofDeg(210.4563, 3.22233);
		assertEquals("(az=210.4563°, alt=3.2223°)", hor3.toString());
	}
}
