package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class EpochTest {

	
	@Test
	public void daysUntilTest() {
		assertTrue(true);
		
		ZonedDateTime td0=ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0), ZoneId.of("UTC"));
		
		assertEquals(0.0, Epoch.J2000.daysUntil(td0));
		
		ZonedDateTime td1=ZonedDateTime.of(LocalDate.of(2000, Month.FEBRUARY, 1), LocalTime.of(12, 0), ZoneId.of("UTC"));
		
		assertEquals(31.0, Epoch.J2000.daysUntil(td1));
		
		ZonedDateTime td2=ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 2), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		assertEquals(0.5, Epoch.J2000.daysUntil(td2));
		
		ZonedDateTime td3=ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 4), LocalTime.of(18, 0), ZoneId.of("UTC"));
		
		assertEquals(3.25, Epoch.J2000.daysUntil(td3));
		
	}
	
	@Test
	public void julianCenturiesUntilTest() {
		ZonedDateTime td1=ZonedDateTime.of(LocalDate.of(2020, Month.FEBRUARY, 24), LocalTime.of(12, 0), ZoneId.of("UTC"));  
		
		assertEquals( 0.20146475017111, Epoch.J2000.julianCenturiesUntil(td1), 1E-4);
		
		ZonedDateTime td2=ZonedDateTime.of(LocalDate.of(2017, Month.MARCH, 8), LocalTime.of(12, 0), ZoneId.of("UTC"));
		
		assertEquals(0.17181382614647, Epoch.J2000.julianCenturiesUntil(td2), 1E-4);
		
		ZonedDateTime td0=ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0), ZoneId.of("UTC"));
		
		assertEquals(0.0, Epoch.J2000.julianCenturiesUntil(td0));
		
		ZonedDateTime td3=ZonedDateTime.of(LocalDate.of(2006, Month.JULY, 11), LocalTime.of(12, 0), ZoneId.of("UTC"));
		
		assertEquals(0.06522929500342 , Epoch.J2000.julianCenturiesUntil(td3), 1E-4);
		
		ZonedDateTime td4=ZonedDateTime.of(LocalDate.of(2010, Month.JULY, 14), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		assertEquals(0.10531143052703, Epoch.J2000.julianCenturiesUntil(td4), 1E-10);
		
		ZonedDateTime td5=ZonedDateTime.of(LocalDate.of(2005, Month.MAY, 23), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		assertEquals(0.05389459274469, Epoch.J2000.julianCenturiesUntil(td5), 1E-10);
		
		ZonedDateTime td6=ZonedDateTime.of(LocalDate.of(2019, Month.MARCH, 15), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		assertEquals(0.19199178644763, Epoch.J2000.julianCenturiesUntil(td6), 1E-10);
		
	}

	
	/**
	 * 			
	 * 	
	 * 
	 */
	
}
