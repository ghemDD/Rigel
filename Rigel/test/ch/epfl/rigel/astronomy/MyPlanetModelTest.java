package ch.epfl.rigel.astronomy;

import static java.time.ZonedDateTime.of;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public class MyPlanetModelTest {

	
	//@Test
	void planetModelTestJupiter() {
		
		//Planet jupi=PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(of(2003,11,22,00,00,00,00, ZoneId.of("UTC"))), new EclipticToEquatorialConversion(of(2001,1,27,00,00,00,00, ZoneId.of("UTC"))));
		
		assertTrue(true);
	}
	
	@Test
	void TelegramTest() {
		
		double test1=PlanetModel.JUPITER.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
		                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
		        .equatorialPos().raHr();
		
		assertEquals(11.187154934709682, test1);
		
		double test2 = PlanetModel.JUPITER.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
		                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
				.equatorialPos().decDeg();
		
		assertEquals(6.356635506685756, test2, 1E-14);
		
		double test3 = Angle.toDeg(PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).angularSize())*3600;
		
		assertEquals(35.11141185362771, test3);
		
		double test4 = PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).magnitude();
		
		assertEquals(-1.9885659217834473, test4);
		
		double test5 = PlanetModel.MERCURY.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
		                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
		        .equatorialPos().raHr();
		
		assertEquals(16.820074565897194, test5, 1E-13);
		
		double test6 = PlanetModel.MERCURY.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
		                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
				.equatorialPos().decDeg();
		
		assertEquals(-24.500872462861274, test6, 1E-13);
		
		
		double test8 = PlanetModel.MERCURY.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
		                        LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
		        .magnitude();
		
		//System.out.println(test8);
        
		double test9=Angle.toDeg(PlanetModel.VENUS.at(-2231.0,
		        new EclipticToEquatorialConversion(
		                ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 13),
		                		LocalTime.of(13, 47, 57), ZoneOffset.UTC)))
		        .angularSize())*3600;
		
		//System.out.println(test9);
		
	}
}
