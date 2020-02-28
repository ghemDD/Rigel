package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
 

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

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
		
		EclipticCoordinates ecli1=EclipticCoordinates.of(ofDeg(78.5000), ofDeg(43.0000));
		assertEquals("(λ=78.5000°, β=43.0000°)", ecli1.toString());
		
	}
	
    @Test
    void ofWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonValue = Math.random() * Angle.TAU;
            double latValue = Math.random() * Angle.TAU/2 - Angle.TAU/4;
            var h = EclipticCoordinates.of(lonValue, latValue);

            assertEquals(lonValue, h.lon(), 1e-6);
            assertEquals(latValue, h.lat(), 1e-6);
        }
    }

    @Test
    void ofFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double lonValuePositive = Angle.TAU + Math.random()*i;
            double latValuePositive = Angle.TAU/4 + Math.random()*i;

            double lonValueNegative = -0.0001-Math.random()*i;
            double latValueNegative = -Angle.TAU/4-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValuePositive, latValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValueNegative, latValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValueNegative, latValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValuePositive, latValueNegative);
            });
        }
    }

    @Test
    void toStringTest(){
        var h1 = EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/4); //(90°, 90°)
        var h2 = EclipticCoordinates.of(Angle.TAU/16, -Angle.TAU/6); //(22.5°, -60°)

        assertEquals("(λ=90.0000°, β=90.0000°)", h1.toString());
        assertEquals("(λ=22.5000°, β=-60.0000°)", h2.toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var coordinates = EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/6);
            coordinates.equals(coordinates);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/6).hashCode();
        });
    }
}
