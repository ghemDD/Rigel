package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import static java.time.ZonedDateTime.*;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

import static ch.epfl.rigel.math.Angle.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SiderealTimeTest {
	
	@Test
	void greenwichSiderealTimeTest() {
		
		//ZonedDateTime td0=ZonedDateTime.of(LocalDate.of(2020, Month.FEBRUARY, 25), LocalTime.of(0, 0), ZoneId.of("UTC"));
		
		//assertEquals(ofDMS(167, 8, 30), SiderealTime.greenwich(td0), 1E-4);
		
		ZonedDateTime td1=ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51, 67), ZoneId.of("UTC"));
		
		SiderealTime.greenwich(td1);
		
		
		System.out.println("****************");
		
		ZonedDateTime td2=ZonedDateTime.of(LocalDate.of(2001, Month.JANUARY, 27), LocalTime.of(12, 00), ZoneId.of("UTC"));
		
		SiderealTime.greenwich(td2);
		
		System.out.println("****************");
		
		ZonedDateTime td3=ZonedDateTime.of(LocalDate.of(2001, Month.SEPTEMBER, 11), LocalTime.of(8, 14), ZoneId.of("UTC"));
		
		SiderealTime.greenwich(td3);
		
		System.out.println("****************");
		
		ZonedDateTime td4=ZonedDateTime.of(LocalDate.of(2001, Month.JANUARY, 27), LocalTime.of(12, 00), ZoneId.of("UTC"));
		
		SiderealTime.greenwich(td4);
		
		System.out.println("****************");
		
		ZonedDateTime td5=ZonedDateTime.of(LocalDate.of(2004, Month.SEPTEMBER, 23), LocalTime.of(11, 00), ZoneId.of("UTC"));
		
		SiderealTime.greenwich(td5);
		
		System.out.println("****************");
		
		assertEquals(0, 0);
	}
	
	@Test
	void Sidg (){
		double test1=SiderealTime.greenwich(of(1980,4,22,14,36,51,67, ZoneId.of("UTC")));
		
		assertEquals(1.2220619247737088, test1);
		
		double test2=SiderealTime.greenwich(of(2001,1,27,12,00,00,00, ZoneId.of("UTC")));
		
		assertEquals(5.355270290366605, test2);
		
		double test3=SiderealTime.greenwich(of(2004,9,23,11,00,00,00, ZoneId.of("UTC")));
		
		assertEquals(2.9257399567031235, test3);
		
		double test4=SiderealTime.greenwich(of(2001,9,11,8,14,00,00, ZoneId.of("UTC")));
		
		assertEquals(1.9883078130455532, test4, 1E-10);
	}
	
	@Test
	void Sidl() {
		
	double test1=SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,27,ZoneId.of("UTC")), 
		    GeographicCoordinates.ofDeg(30,45));
		
	double test2=SiderealTime.greenwich(of(1980,4,22,14,36,51,67, ZoneId.of("UTC")));
	
		assertEquals(test1, test2+Math.toRadians(30));
		assertEquals(1.74570958832716, test2+Angle.ofDeg(30), 1E-5);
		assertEquals(1.74570958832716, test1, 1E-5);
	}
	/**
	 * 
	 * greenwichTest sidereal time
	 * localTestonKnownLocations
	 *  
	 */

}
