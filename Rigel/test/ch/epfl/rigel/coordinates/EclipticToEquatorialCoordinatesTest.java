package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class EclipticToEquatorialCoordinatesTest {
	
	@Test
	void conversionWorks() {

		double test1=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0,0,ZoneOffset.UTC))
			.apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31))).raHr();

		assertEquals(9.581478170200256, test1);
		
		double test2=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0,0,ZoneOffset.UTC))
	      .apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31))).dec();
		
		assertEquals(0.34095012064184566, test2);
		
		
		
	}

}
