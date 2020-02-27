package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import ch.epfl.rigel.astronomy.*;
import static ch.epfl.rigel.math.Angle.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SiderealTimeTest {
	
	@Test
	void greenwichSiderealTimeTest() {
		
		ZonedDateTime td0=ZonedDateTime.of(LocalDate.of(2020, Month.FEBRUARY, 25), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		//assertEquals(ofDMS(167, 8, 30), SiderealTime.greenwich(td0), 1E-4);
		
		ZonedDateTime td1=ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, 67), ZoneId.of("UTC"));
		
		assertEquals(ofDMS((int) toDeg(ofHr(4)), 8, 30), SiderealTime.greenwich(td1), 1E-4);
	}
	
	
	/**
	 * 
	 * greenwichTest sidereal time
	 * localTestonKnownLocations
	 * 
	 */

}
