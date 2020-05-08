package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;

public class MyEclipticToEquatorialCoordinatesTest {
	
	//obliquité good
	@Test
	void conversionWorks() {
		//Precision of the obliquity is causing the loss
		//Repercussion on sin dec
		
		//double test1=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0,0,ZoneOffset.UTC))
			//.apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10), Angle.ofDMS(4,52,31))).ra();
		
		EquatorialCoordinates c=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0,0,ZoneOffset.UTC))
				.apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10), Angle.ofDMS(4,52,31)));

		//RaHrassertEquals(9.581478170200256, test1);
		//assertEquals(2.508430993973960, test1, 1E-5);
		assertEquals(c.dec(), Angle.ofDMS(19, 32, 6.01));
		assertEquals(c.ra(), Angle.ofHr(9+34/60.0+53.32/3600.0));
		
		double test2=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,7,6,0,0,0,0,ZoneOffset.UTC))
	      .apply(EclipticCoordinates.of(Angle.ofDMS(139,41,10),Angle.ofDMS(4,52,31))).dec();
		
		assertEquals(0.34095012064184566, test2);
		
		System.out.println("\n");
		double test3=new  EclipticToEquatorialConversion(ZonedDateTime.of(2010,7,10,0,0,0,0,ZoneOffset.UTC))
				.apply(EclipticCoordinates.of(Angle.ofDMS(97,35,10), Angle.ofDMS(16,58,27))).ra();

			//RaHrassertEquals(9.581478170200256, test1);
		assertEquals(1.736778252150390, test3, 1E-5);
			
		System.out.println("\n");
		double test4=new  EclipticToEquatorialConversion(ZonedDateTime.of(2010,7,10,0,0,0,0,ZoneOffset.UTC))
					.apply(EclipticCoordinates.of(Angle.ofDMS(97,35,10), Angle.ofDMS(16,58,27))).dec();
			
		assertEquals(0.700965701656261, test4, 1E-5);
		
		System.out.println("\n");
		double test5=new  EclipticToEquatorialConversion(ZonedDateTime.of(2006,11,19,0,0,0,0,ZoneOffset.UTC))
				.apply(EclipticCoordinates.of(Angle.ofDMS(72,22,8), Angle.ofDMS(49,3,12))).ra();

			//RaHrassertEquals(9.581478170200256, test1);
		//assertEquals(0.941341278977022, test5, 1E-4);
			
		System.out.println("\n");
		double test6=new  EclipticToEquatorialConversion(ZonedDateTime.of(2006,11,19,0,0,0,0,ZoneOffset.UTC))
					.apply(EclipticCoordinates.of(Angle.ofDMS(72,22,8), Angle.ofDMS(49,3,12))).dec();
			
		assertEquals(1.226876758986660, test6, 1E-4);
		
		double test7=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,12,3,0,0,0,0,ZoneOffset.UTC))
				.apply(EclipticCoordinates.of(Angle.ofDMS(52,32,16), Angle.ofDMS(89,3,12))).ra();

			//RaHrassertEquals(9.581478170200256, test1);
		//assertEquals(0.941341278977022, test5, 1E-4);
			
		System.out.println("\n");
		double test8=new  EclipticToEquatorialConversion(ZonedDateTime.of(2009,12,3,0,0,0,0,ZoneOffset.UTC))
					.apply(EclipticCoordinates.of(Angle.ofDMS(52,32,16), Angle.ofDMS(89,3,12))).dec();
			
		//assertEquals(1.226876758986660, test6, 1E-4);
	}

}
