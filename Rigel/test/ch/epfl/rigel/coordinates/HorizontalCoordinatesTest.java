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
	
	   @Test
	    void ofWorksWithValidValues(){
	        for(int i=0; i<1000; ++i) {
	            double azValue = Math.random() * Angle.TAU;
	            double altValue = Math.random() * Angle.TAU/2 - Angle.TAU/4;
	            var h = HorizontalCoordinates.of(azValue, altValue);

	            assertEquals(azValue, h.az(), 1e-6);
	            assertEquals(altValue, h.alt(), 1e-6);
	        }
	    }

	    @Test
	    void ofFailsWithInvalidValues(){
	        for(int i=0; i<1000; ++i){
	            double azValuePositive = Angle.TAU + Math.random()*i;
	            double altValuePositive = Angle.TAU/4 + Math.random()*i;

	            double azValueNegative = -0.0001-Math.random()*i;
	            double altValueNegative = -Angle.TAU/4-Math.random()*i;

	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.of(azValuePositive, altValuePositive);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.of(azValueNegative, altValueNegative);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.of(azValueNegative, altValuePositive);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.of(azValuePositive, altValueNegative);
	            });
	        }
	    }

	    @Test
	    void ofDegWorksWithValidValues(){
	        for(int i=0; i<1000; ++i) {
	            double azValue = Math.random() * 360.0;
	            double altValue = Math.random() * 180.0 - 90.0;
	            var h = HorizontalCoordinates.ofDeg(azValue, altValue);

	            assertEquals(azValue, h.azDeg(), 1e-6);
	            assertEquals(altValue, h.altDeg(), 1e-6);
	        }
	    }

	    @Test
	    void ofDegFailsWithInvalidValues(){
	        for(int i=0; i<1000; ++i){
	            double azValuePositive = 360.0 + Math.random()*i;
	            double altValuePositive = 90.0001 + Math.random()*i;

	            double azValueNegative = -0.0001-Math.random()*i;
	            double altValueNegative = -90.0001-Math.random()*i;

	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.ofDeg(azValuePositive, altValuePositive);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.ofDeg(azValueNegative, altValueNegative);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.ofDeg(azValueNegative, altValuePositive);
	            });
	            assertThrows(IllegalArgumentException.class, () -> {
	                HorizontalCoordinates.ofDeg(azValuePositive, altValueNegative);
	            });
	        }
	    }

	    @Test
	    void azOctantNameTest(){
	        var h1 = HorizontalCoordinates.ofDeg(337.5, 90); //NORD
	        var h11 = HorizontalCoordinates.ofDeg(0, 90); //NORD
	        var h111 = HorizontalCoordinates.ofDeg(22.4, 90); //NORD

	        var h2 = HorizontalCoordinates.ofDeg(22.5, 90); //NORD-EST
	        var h22 = HorizontalCoordinates.ofDeg(45, 90); //NORD-EST
	        var h222 = HorizontalCoordinates.ofDeg(67.4, 90); //NORD-EST

	        var h3 = HorizontalCoordinates.ofDeg(67.5, 90); //EST
	        var h33 = HorizontalCoordinates.ofDeg(90, 90); //EST
	        var h333 = HorizontalCoordinates.ofDeg(112.4, 90); //EST

	        var h4 = HorizontalCoordinates.ofDeg(112.5, 90); //SUD-EST
	        var h44 = HorizontalCoordinates.ofDeg(135, 90); //SUD-EST
	        var h444 = HorizontalCoordinates.ofDeg(157.4, 90); //SUD-EST

	        var h5 = HorizontalCoordinates.ofDeg(157.5, 90); //SUD
	        var h55 = HorizontalCoordinates.ofDeg(180, 90); //SUD
	        var h555 = HorizontalCoordinates.ofDeg(202.4, 90); //SUD

	        var h6 = HorizontalCoordinates.ofDeg(202.5, 90); //SUD-OUEST
	        var h66 = HorizontalCoordinates.ofDeg(225, 90); //SUD-OUEST
	        var h666 = HorizontalCoordinates.ofDeg(247.4, 90); //SUD-OUEST

	        var h7 = HorizontalCoordinates.ofDeg(247.5, 90); //OUEST
	        var h77 = HorizontalCoordinates.ofDeg(270, 90); //OUEST
	        var h777 = HorizontalCoordinates.ofDeg(292.4, 90); //OUEST

	        var h8 = HorizontalCoordinates.ofDeg(292.5, 90); //NORD-OUEST
	        var h88 = HorizontalCoordinates.ofDeg(315, 90); //NORD-OUEST
	        var h888 = HorizontalCoordinates.ofDeg(337.4, 90); //NORD-OUEST

	        assertEquals("N", h1.azOctantName("N", "E", "S", "O"));
	        assertEquals("N", h11.azOctantName("N", "E", "S", "O"));
	        assertEquals("N", h111.azOctantName("N", "E", "S", "O"));

	        assertEquals("NE", h2.azOctantName("N", "E", "S", "O"));
	        assertEquals("NE", h22.azOctantName("N", "E", "S", "O"));
	        assertEquals("NE", h222.azOctantName("N", "E", "S", "O"));

	        assertEquals("E", h3.azOctantName("N", "E", "S", "O"));
	        assertEquals("E", h33.azOctantName("N", "E", "S", "O"));
	        assertEquals("E", h333.azOctantName("N", "E", "S", "O"));

	        assertEquals("SE", h4.azOctantName("N", "E", "S", "O"));
	        assertEquals("SE", h44.azOctantName("N", "E", "S", "O"));
	        assertEquals("SE", h444.azOctantName("N", "E", "S", "O"));

	        assertEquals("S", h5.azOctantName("N", "E", "S", "O"));
	        assertEquals("S", h55.azOctantName("N", "E", "S", "O"));
	        assertEquals("S", h555.azOctantName("N", "E", "S", "O"));

	        assertEquals("SO", h6.azOctantName("N", "E", "S", "O"));
	        assertEquals("SO", h66.azOctantName("N", "E", "S", "O"));
	        assertEquals("SO", h666.azOctantName("N", "E", "S", "O"));

	        assertEquals("O", h7.azOctantName("N", "E", "S", "O"));
	        assertEquals("O", h77.azOctantName("N", "E", "S", "O"));
	        assertEquals("O", h777.azOctantName("N", "E", "S", "O"));

	        assertEquals("NO", h8.azOctantName("N", "E", "S", "O"));
	        assertEquals("NO", h88.azOctantName("N", "E", "S", "O"));
	        assertEquals("NO", h888.azOctantName("N", "E", "S", "O"));

	    }

	    @Test
	    void angularDistanceToTest(){
	        var first1 = HorizontalCoordinates.ofDeg(6.5682, 46.5183);
	        var second1 = HorizontalCoordinates.ofDeg(8.5476, 47.3763);

	        var first2 = HorizontalCoordinates.ofDeg(6.07675, 46.177101);
	        var second2 = HorizontalCoordinates.ofDeg(8.54169, 47.376888);

	        var first3 = HorizontalCoordinates.ofDeg(6.07675, 46.177101);
	        var second3 = HorizontalCoordinates.ofDeg(6.632273, 46.519653);

	        assertEquals(0.0279, first1.angularDistanceTo(second1), 1e-4);
	        assertEquals(0.0361440235, first2.angularDistanceTo(second2), 1e-6);
	        assertEquals(0.00897448301, first3.angularDistanceTo(second3), 1e-6);
	    }

	    @Test
	    void toStringTest(){
	        var h1 = HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/4); //(90°, 90°)
	        var h2 = HorizontalCoordinates.of(Angle.TAU/16, -Angle.TAU/6); //(22.5°, -60°)
	        var h3 = HorizontalCoordinates.ofDeg(2.67984205, 3.642014042);
	        var h4 = HorizontalCoordinates.ofDeg(350, 7.2);

	        assertEquals("(az=90.0000°, alt=90.0000°)", h1.toString());
	        assertEquals("(az=22.5000°, alt=-60.0000°)", h2.toString());
	        assertEquals("(az=2.6798°, alt=3.6420°)", h3.toString());
	        assertEquals("(az=350.0000°, alt=7.2000°)", h4.toString());
	    }

	    @Test
	    void equalsThrowsUOE() {
	        assertThrows(UnsupportedOperationException.class, () -> {
	            var coordinates = HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/6);
	            coordinates.equals(coordinates);
	        });
	    }

	    @Test
	    void hashCodeThrowsUOE() {
	        assertThrows(UnsupportedOperationException.class, () -> {
	            HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/6).hashCode();
	        });
	    }
}
