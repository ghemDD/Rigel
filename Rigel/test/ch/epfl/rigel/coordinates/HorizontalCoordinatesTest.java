package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.coordinates.HorizontalCoordinates.ofDeg;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class HorizontalCoordinatesTest {
	//Round the value of the angular distance ?
	//Fix the comma in toString() method

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
		
		assertEquals("N", HorizontalCoordinates.ofDeg(0, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("NE", HorizontalCoordinates.ofDeg(40, 0)
				.azOctantName("N", "E", "S", "O"));
		
		assertEquals("E", HorizontalCoordinates.ofDeg(90, 0).azOctantName("N", "E", "S", "O"));
		
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
		
		HorizontalCoordinates paris=HorizontalCoordinates.ofDeg(48.8534, 2.3488);
		double dist2=paris.angularDistanceTo(epfl);

		assertEquals(1.022, dist2, 1E-4);
	}

	@Test
	void toStringWorksOnKnownHorizontalCoordinates() {
		HorizontalCoordinates hor=HorizontalCoordinates.ofDeg(350, 7.2);
		assertEquals(Locale.ROOT, "(az=350.0000°, alt=7.2000°)", hor.toString());
	}
}
