package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.test.TestRandomizer;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static ch.epfl.rigel.coordinates.GeographicCoordinates.*;

import org.junit.jupiter.api.Test;

public class GeographicCoordinatesTest {

	@Test 
	void ofDegFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			GeographicCoordinates geo=GeographicCoordinates.ofDeg(432, 767);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			GeographicCoordinates geo=GeographicCoordinates.ofDeg(890, 990);
		});
	}

	@Test
	void isValidLonDegReturnsTrue() {
		double value=0.0;
		for(int i=0; i<99; ++i) {
			value=ThreadLocalRandom.current().nextDouble(LONGITUDE_INT.low(), LONGITUDE_INT.high());
			assertTrue(isValidLonDeg(value));
		}
	}

	@Test
	void isValidLonDegReturnsFalse() {
		assertFalse(isValidLonDeg(390));
		assertFalse(isValidLonDeg(-181));
		assertFalse(isValidLonDeg(181));
		assertFalse(isValidLonDeg(787));
		assertFalse(isValidLonDeg(-184));
	}

	@Test
	void isValidLonDegLimit() {
		assertFalse(isValidLonDeg(180));
		assertTrue(isValidLonDeg(-180));
	}

	@Test
	void isValidLatDegReturnsTrue() {
		double value=0.0;
		for(int i=0; i<TestRandomizer.RANDOM_ITERATIONS; ++i) {
			value=ThreadLocalRandom.current().nextDouble(LATITUDE_INT.low(), LATITUDE_INT.high());
			assertTrue(isValidLatDeg(value));
		}
	}

	@Test
	void isValidLatDegReturnFalse() {
		assertFalse(isValidLatDeg(400));
		assertFalse(isValidLatDeg(91));
		assertFalse(isValidLatDeg(-98));
	}
	
	@Test
	void isValidLatDegLimit() {
		assertTrue(isValidLatDeg(90));
		assertTrue(isValidLatDeg(-90));
	}

	@Test 
	void gettersLatWellDefined() {
		GeographicCoordinates geo1=ofDeg(29, 57);
		GeographicCoordinates geo2=ofDeg(45, 87);
		
		assertEquals(Angle.ofDeg(29), geo1.lon());
		assertEquals(Angle.ofDeg(57), geo1.lat());
		assertEquals(29, geo1.lonDeg(), 1e-5);
		assertEquals(57, geo1.latDeg(), 1e-5);
		
		assertEquals(Angle.ofDeg(45), geo2.lon());
		assertEquals(Angle.ofDeg(87), geo2.lat());
		assertEquals(45, geo2.lonDeg(), 1e-5);
		assertEquals(87, geo2.latDeg(), 1e-5);
	}

	@Test
	void toStringWorksOnKnownGeographicalCoordinates() {
		GeographicCoordinates geo=GeographicCoordinates.ofDeg(6.5700, 46.5200);
		assertEquals("(lon=6.5700°, lat=46.5200°)", geo.toString());
		
		GeographicCoordinates geo1=GeographicCoordinates.ofDeg(6.5700, 46.5200);
		assertEquals("(lon=6.5700°, lat=46.5200°)", geo1.toString());
		
		GeographicCoordinates geo2=GeographicCoordinates.ofDeg(89.98321, 34.56);
		assertEquals("(lon=89.9832°, lat=34.5600°)", geo2.toString());
		
		GeographicCoordinates geo3=GeographicCoordinates.ofDeg(78.3452, 46.52212);
		assertEquals("(lon=78.3452°, lat=46.5221°)", geo3.toString());
	}
	
	@Test
	void toStringWorks(){
		assertEquals("(lon=6.5700°, lat=46.5200°)",GeographicCoordinates.ofDeg(6.57, 46.52).toString());
		
		assertEquals("(lon=6.5700°, lat=-90.0000°)",GeographicCoordinates.ofDeg(6.57, -90).toString());
		assertEquals("(lon=6.5700°, lat=90.0000°)",GeographicCoordinates.ofDeg(6.57, 90).toString());
		assertEquals("(lon=-180.0000°, lat=67.0000°)",GeographicCoordinates.ofDeg(-180, 67).toString());
		assertEquals("(lon=-6.5700°, lat=-46.5200°)",GeographicCoordinates.ofDeg(-6.57, -46.52).toString());
		assertEquals("(lon=-179.9000°, lat=90.0000°)",GeographicCoordinates.ofDeg(-179.9, 90).toString());
		assertEquals("(lon=-180.0000°, lat=-90.0000°)",GeographicCoordinates.ofDeg(-180, -90).toString());
	}
	
	@Test
	void isValideLonDegWorks() {
		for(double i =-180.00000; i<180; i +=0.00001) {
			assertTrue(GeographicCoordinates.isValidLonDeg(i));
		}
		
		for(double i =-1000; i<-180; i+=0.00001) {
			assertFalse(GeographicCoordinates.isValidLonDeg(i));
		}
		for(double i =180; i<1000; i+=0.00001) {
			assertFalse(GeographicCoordinates.isValidLonDeg(i));
		}
	}
	
	@Test
	void isValideLatDeg() {
		for(double i =-90.00000; i<=90; i +=0.00001) {
			assertTrue(GeographicCoordinates.isValidLatDeg(i));
		}
		
		for(double i =-1000; i<-90.00000; i+=0.00001) {
			assertFalse(GeographicCoordinates.isValidLatDeg(i));
		}
		for(double i =90.0001; i<1000; i+=0.00001) {
			assertFalse(GeographicCoordinates.isValidLatDeg(i));
		}
		
	}
	
    @Test
    void testAngleOutOfBounds(){
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(20, 215);});
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(-200, 15);});
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(-181, 90);});

    }

    @Test
    void ofDegWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;
            double latVal = Math.random() * 180.0 - 90.0;
            var h = ofDeg(lonVal, latVal);

            assertEquals(lonVal, h.lonDeg(), 1e-6);
            assertEquals(latVal, h.latDeg(), 1e-6);
        }
    }

    @Test
    void getterWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;
            double latVal = Math.random() * 180.0 - 90.0;
            var h = ofDeg(lonVal, latVal);

            assertEquals(Angle.ofDeg(lonVal), h.lon(), 1e-6);
            assertEquals(Angle.ofDeg(latVal), h.lat(), 1e-6);
        }
    }

    @Test
    void ofDegFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double azValuePositive = 180 + Math.random()*i;
            double altValuePositive = 90.0001 + Math.random()*i;

            double azValueNegative = -0.0001-Math.random()*i;
            double altValueNegative = -90.0001-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValuePositive, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValueNegative, altValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValueNegative, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValuePositive, altValueNegative);
            });
        }
    }
    @Test
    void toStringTest(){
        var h1 = GeographicCoordinates.ofDeg(90, 90); //(90°, 90°)
        var h2 = GeographicCoordinates.ofDeg(22.5, -60); //(22.5°, -60°)
        var h3 = GeographicCoordinates.ofDeg(2.67984205, 3.642014042);
        var h4 = GeographicCoordinates.ofDeg(35, 7.2);
        var h5 = GeographicCoordinates.ofDeg(6.57, 46.52);


        assertEquals("(lon=90.0000°, lat=90.0000°)", h1.toString());
        assertEquals("(lon=22.5000°, lat=-60.0000°)", h2.toString());
        assertEquals("(lon=2.6798°, lat=3.6420°)", h3.toString());
        assertEquals("(lon=35.0000°, lat=7.2000°)", h4.toString());
        assertEquals("(lon=6.5700°, lat=46.5200°)", h5.toString());

    }


    @Test
    void isValidLonDegWorksWithValidValues(){
        for(int i=0; i<100000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;

            assertTrue(isValidLonDeg(lonVal));        }
    }

    @Test
    void isValidLatDegWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double latVal = Math.random() * 180.0 - 90.0;

            assertTrue(isValidLatDeg(latVal));
        }
    }

    @Test
    void isValidLonDegNotWorksWithInvalidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = 180 + Math.random()*i;
            double lonVal2 = -1 * (180 + Math.random()*i);

            assertFalse(isValidLonDeg(lonVal));
            assertFalse(isValidLonDeg(lonVal));        }
    }

}
