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

public class MyEquatorialToHorizontalCoordinatesTest {
	
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
