package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public class MyMoonModelTest {

	
	@Test
	void TelegramTest() {
		
		double test1 = MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().raHr();
		
		assertEquals(14.211456457835897, test1);
		
		//System.out.println(test1);
		/**
		 * 14.211456457836277
		 * 14.211456457835897
		 * Mine :
		 * 14.211456460151235
		 * 14.211456460150915
		 */
		
		double test2 = MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003,  Month.SEPTEMBER, 1),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().dec();
		
		assertEquals(-0.20114171346014934, test2);
		
		//System.out.println(test2);
		
		/**
		 * -0.20114171346014934
		 * -0.20114171346019355
		 * Mine :
		 * -0.20114171368989492
		 */
		
		/**
		 * Angular Size test
		 */
		double test3 =  MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),
		        ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of(
		        LocalDate.of(1979, 9, 1),LocalTime.of(0, 0),ZoneOffset.UTC))).
		        angularSize();
		        		
		assertEquals(0.009225908666849136, test3);
		    
		/**
		 * Phase test
		 */
		String test4 = MoonModel.MOON.at(Epoch.J2010.daysUntil(ZonedDateTime.of(LocalDate.of(2003, 9, 1),LocalTime.of(0, 0),
		        ZoneOffset.UTC)), new EclipticToEquatorialConversion(ZonedDateTime.of( LocalDate.of(2003, 9, 1),
		        LocalTime.of(0, 0),ZoneOffset.UTC))).
		        info(); 
		        		
		      assertEquals("Lune (22.5%)", test4);
	}
}
