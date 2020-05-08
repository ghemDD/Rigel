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

public class MySiderealTimeTest {
	
	@Test
	void Sidg (){
		//double test1=SiderealTime.greenwich(of(1980,4,22,14,36,51,(int) 67, ZoneId.of("UTC")));
		ZonedDateTime td1=ZonedDateTime.of(LocalDate.of(1980, Month.APRIL, 22), LocalTime.of(14, 36, 51,  (int) (67E7)), ZoneId.of("UTC"));
		System.out.println(td1.toString());
		double test=SiderealTime.greenwich(td1);
		System.out.println("                            "+ofHr(4.668119326877550));

		assertEquals(ofHr(4.668119326877550), test, 1E-12);
		//assertEquals(1.2220619247737088, test1);
		//1.2221108127288611
		//4,668119326877550

		
		System.out.println(of(2001,1,27,12,00,00,00, ZoneId.of("UTC")).toString());
		double test2=SiderealTime.greenwich(of(2001,1,27,12,00,00,00, ZoneId.of("UTC")));
		System.out.println("                            "+ofHr(20.455625719320400));
		System.out.println("\n");
		
		assertEquals(ofHr(20.455625719320400), test2, 1E-10);
		
		System.out.println(of(2001,9,23,11,00,00,00, ZoneId.of("UTC")));
		double test3=SiderealTime.greenwich(of(2001,9,23,11,00,00,00, ZoneId.of("UTC")));
		System.out.println("                            "+ofHr(11.157535845281400));
		System.out.println("\n");
		
		assertEquals(ofHr(11.157535845281400), test3, 1E-10);
		
		System.out.println(of(2001,9,11,8,14,00,00, ZoneId.of("UTC")));
		double test4=SiderealTime.greenwich(of(2001,9,11,8,14,00,00, ZoneId.of("UTC")));
		System.out.println("                            "+ofHr(7.594776404026460));
		System.out.println("\n");
		
		assertEquals(ofHr(7.594776404026460), test4, 1E-10);
		
		
		double test5=SiderealTime.greenwich(of(2017,3,8,12,00,00,00, ZoneId.of("UTC")));
		
		System.out.println(test5+"     "+ofHr(23.092233215565100));
		assertEquals(ofHr(23.092233215565100), test5, 1E-10);
		
		double test6=SiderealTime.greenwich(of(2016,4,10,12,00,00,00, ZoneId.of("UTC")));
		assertEquals(ofHr(1.276571438031740), test6, 1E-10);
	
	}
	
	
	//@Test
	void Sidl() {
		
	double test1=SiderealTime.local(ZonedDateTime.of(1980,4,22,14,36,51,27,ZoneId.of("UTC")), 
		    GeographicCoordinates.ofDeg(30,45));
		
	double test2=SiderealTime.greenwich(of(1980,4,22,14,36,51,67, ZoneId.of("UTC")));
	
		assertEquals(test1, test2+Math.toRadians(30));
		assertEquals(1.74570958832716, test2+Angle.ofDeg(30), 1E-5);
		assertEquals(1.74570958832716, test1, 1E-5);
	}

}
