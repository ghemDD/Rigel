package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

import static ch.epfl.rigel.math.Angle.*;

public class EquatorialToHorizontalCoordinatesTest {

	
	@Test
	void conversionWork() {
		
		//Book test
		EquatorialCoordinates t=EquatorialCoordinates.of(0.0, ofDMS(23, 13, 10));
		HorizontalCoordinates hori=new EquatorialToHorizontalConversion(ofHr(5+51.0/60.0+44.0/3600.0), ofDeg(52))
									.applyTest(t);
		
		assertEquals(ofDMS(283, 16, 15.70), hori.az(), 1E-7);
		assertEquals(ofDMS(19, 20, 3.64), hori.alt(), 1E-7);
		
		EquatorialCoordinates t1=EquatorialCoordinates.of(0.0, ofDMS(15, 9, 5));
		HorizontalCoordinates hori1=new EquatorialToHorizontalConversion(ofHr(7+25.0/60.0+12.0/3600.0), ofDeg(44))
									.applyTest(t1);
		
		assertEquals(ofDeg(295.636447951882000), hori1.az(), 1E-14);
		assertEquals(-0.070713698311482, hori1.alt(), 1E-14);
		
		
		EquatorialCoordinates t2=EquatorialCoordinates.of(0.0, ofDMS(34, 17, 7));
		HorizontalCoordinates hori2=new EquatorialToHorizontalConversion(ofHr(13+13.0/60.0+45.0/3600.0), ofDeg(29))
									.applyTest(t2);
		
		assertEquals(Math.toRadians(16.669581495086500), hori2.az(), 1E-14);
		assertEquals(-0.425146697797754, hori2.alt(), 1E-14);
		
		
		
		}
	
	
	@Test
	public void newTestAz() {
	LocalDate t= LocalDate.of(2001,  Month.APRIL, 16);
	ZonedDateTime when= ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
	GeographicCoordinates where= GeographicCoordinates.ofDeg(20, 52);
	EquatorialToHorizontalConversion converter= new EquatorialToHorizontalConversion(when, where);
	EquatorialCoordinates eqCoord= EquatorialCoordinates.of(0.25, 0.8);
	double actualAz= converter.apply(eqCoord).az();
	assertEquals(5.0538984328743, actualAz, 1E-8);
	}
}
