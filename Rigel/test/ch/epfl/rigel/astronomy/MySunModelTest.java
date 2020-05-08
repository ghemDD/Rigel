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
import ch.epfl.rigel.math.Angle;

public class MySunModelTest {
	
	//@Test
	void SunModelWorksOnKnownSolarPosition() {
		
		//Sun s=SunModel.SUN.at(Epoch.J2010.daysUntil(of(2003,7,27,00,00,00,00, ZoneId.of("UTC"))), new EclipticToEquatorialConversion(of(2001,1,27,00,00,00,00, ZoneId.of("UTC"))));
		double test2 = SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 
		        27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).meanAnomaly();
		
		//System.out.println(Angle.toDeg(test2));
		
		assertTrue(true);
	}
	
	@Test
	void TelegramTest() {
		
		
		double test1 = SunModel.SUN.at(27 + 31, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2010,  Month.FEBRUARY, 27),LocalTime.of(0,0), ZoneOffset.UTC))).equatorialPos().ra();
		
		assertEquals(5.9325494700300885, test1);
		
		double test2 = SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 
		        27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().raHr();
		
		assertEquals(8.392682808297807, test2);
		
		double test3 =  SunModel.SUN.at(-2349, new EclipticToEquatorialConversion(ZonedDateTime.of(LocalDate.of(2003, Month.JULY, 
		        27), LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC))).equatorialPos().decDeg();
		
		assertEquals(19.35288373097352, test3);
		
		ZonedDateTime zdt = ZonedDateTime.of(LocalDate.of(1988, Month.JULY, 27), LocalTime.of(0, 0), ZoneOffset.UTC);
	    double test4 = SunModel.SUN.at(Epoch.J2010.daysUntil(zdt), new EclipticToEquatorialConversion(zdt)).equatorialPos().dec();
	       // *0.3353207024580374[4]¨
		
	    assertEquals(0.3353207024580374, test4);
	}
	
	//@Test
	void SunTest() {
		
		ZonedDateTime ZDT_BOOKEXAMPLE = ZonedDateTime.of(
	            LocalDate.of(2003, Month.JULY, 27),
	            LocalTime.of(0, 0, 0),
	            ZoneOffset.UTC
	            );

	    ZonedDateTime ZDT_STARTSEMESTER = ZonedDateTime.of(
	            LocalDate.of(2020, Month.FEBRUARY, 17),
	            LocalTime.of(8, 15, 47),
	            ZoneOffset.UTC
	    );

	   ZonedDateTime ZDT_ENDSEMESTER = ZonedDateTime.of(
	            LocalDate.of(2020, Month.MAY, 29),
	            LocalTime.of(16, 45, 59),
	            ZoneOffset.UTC
	    );

	  ZonedDateTime ZDT_BERLIN_WALL = ZonedDateTime.of(
	            LocalDate.of(1989, Month.NOVEMBER, 9),
	            LocalTime.of(19, 05, 36),
	            ZoneOffset.UTC
	    );

	  ZonedDateTime ZDT_NEW_CENTURY = ZonedDateTime.of(
	            LocalDate.of(2000, Month.JANUARY, 1),
	            LocalTime.of(3, 27, 18),
	            ZoneOffset.UTC
	    );
	    
	    
		double days1=Epoch.J2010.daysUntil(ZDT_BOOKEXAMPLE);
		double days2=Epoch.J2010.daysUntil(ZDT_STARTSEMESTER);
		double days3=Epoch.J2010.daysUntil(ZDT_ENDSEMESTER);
		double days4=Epoch.J2010.daysUntil(ZDT_BERLIN_WALL);
		double days5=Epoch.J2010.daysUntil(ZDT_NEW_CENTURY);
		
		double test1 = SunModel.SUN.at(days1, new EclipticToEquatorialConversion(ZDT_BOOKEXAMPLE)).meanAnomaly();
		double test1p = SunModel.SUN.at(days1, new EclipticToEquatorialConversion(ZDT_BOOKEXAMPLE)).angularSize();
		
		assertEquals(-40.47140884399414, test1);
		assertEquals(0.009161771275103092, test1p);
		
		
		double test2 = SunModel.SUN.at(days2, new EclipticToEquatorialConversion(ZDT_STARTSEMESTER)).meanAnomaly();
		double test2p = SunModel.SUN.at(days2, new EclipticToEquatorialConversion(ZDT_STARTSEMESTER)).angularSize();
		
		assertEquals(63.594200134277344, test2);
		assertEquals(0.009417375549674034, test2p);
		
		
		double test3 = SunModel.SUN.at(days3, new EclipticToEquatorialConversion(ZDT_ENDSEMESTER)).meanAnomaly();
		double test3p = SunModel.SUN.at(days3, new EclipticToEquatorialConversion(ZDT_ENDSEMESTER)).angularSize();
		
		
		assertEquals(65.35498046875, test3);
		assertEquals(0.00917903333902359, test3p);
		
		double test4 = SunModel.SUN.at(days4, new EclipticToEquatorialConversion(ZDT_BERLIN_WALL)).meanAnomaly();
		double test4p = SunModel.SUN.at(days4, new EclipticToEquatorialConversion(ZDT_BERLIN_WALL)).angularSize();
		
		
		assertEquals(-126.60930633544922, test4);
		assertEquals(0.009394984692335129, test4p);
		
		double test5 = SunModel.SUN.at(days5, new EclipticToEquatorialConversion(ZDT_NEW_CENTURY)).meanAnomaly();
		double test5p = SunModel.SUN.at(days5, new EclipticToEquatorialConversion(ZDT_NEW_CENTURY)).angularSize();
		
		assertEquals(-62.88417053222656, test5);
		assertEquals(0.009462689980864525, test5p);
		
	}
	
    // Convert an angle given in HMS to hours.
    private static double hmsToHr(int h, int m, double s) {
        return h + (m / 60d) + (s / 3600d);
    }

    // Same, but when using degrees (name is clearer).
    private static double dmsToDeg(int d, int m, double s) {
        return d + (m / 60d) + (s / 3600d);
    }

    @Test
    void sunEquatorialPosIsCorrectForBookExample() {
        // PACS4, §46 (p. 105)
        var when = ZonedDateTime.of(
                LocalDate.of(2003, Month.JULY, 27),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC);
        var eclToEqu = new EclipticToEquatorialConversion(when);
        var daysSinceJ2010 = Epoch.J2010.daysUntil(when);

        var sunEclPos = SunModel.SUN.at(daysSinceJ2010, eclToEqu).eclipticPos();
        assertEquals(123.580_601, sunEclPos.lonDeg(), 0.000_001d / 2d);
        assertEquals(0, sunEclPos.latDeg());
    }

    @Test
    void sunAngularSizeIsCorrectForBookExample() {
        // PACS4, §48 (p. 110)
        // Beware, it seems this example uses the more precise computation of the Sun's orbit given in §47.
        // (still, the angular size is the same).
        var when = ZonedDateTime.of(
                LocalDate.of(1988, Month.JULY, 27),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC);
        var daysSinceJ2010 = Epoch.J2010.daysUntil(when);
        var eclToEqu = new EclipticToEquatorialConversion(when);

        var sunAngularSize = SunModel.SUN.at(daysSinceJ2010, eclToEqu).angularSize();
        assertEquals(
                dmsToDeg(0, 31, 30),
                Angle.toDeg(sunAngularSize),
                dmsToDeg(0, 0, 0.5));
    }


    @Test
    void sunEclipticPosIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var ecl1 = SunModel.SUN.at(d1, eclToEqu1).eclipticPos();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var ecl2 = SunModel.SUN.at(d2, eclToEqu2).eclipticPos();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var ecl3 = SunModel.SUN.at(d3, eclToEqu3).eclipticPos();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var ecl4 = SunModel.SUN.at(d4, eclToEqu4).eclipticPos();

        assertEquals(1.3004346539729514, ecl1.lon(), 1e-8);
        assertEquals(3.4599344864383497, ecl2.lon(), 1e-8);
        assertEquals(4.894296774140312, ecl3.lon(), 1e-8);
        assertEquals(0.12962120393768117, ecl4.lon(), 1e-8);

        assertEquals(0, ecl1.lat());
        assertEquals(0, ecl2.lat());
        assertEquals(0, ecl3.lat());
        assertEquals(0, ecl4.lat());
    }

    @Test
    void sunAngularSizeIsCorrectForKnownValues() {
        var zdt1 = ZonedDateTime.parse("2007-06-05T12:34:56Z");
        var zdt2 = ZonedDateTime.parse("2009-10-11T12:13:14+02");
        var zdt3 = ZonedDateTime.parse("2011-01-01T00:00:00-05");
        var zdt4 = ZonedDateTime.parse("2020-03-27T17:00:00+01:30");

        var d1 = Epoch.J2010.daysUntil(zdt1);
        var eclToEqu1 = new EclipticToEquatorialConversion(zdt1);
        var angSz1 = SunModel.SUN.at(d1, eclToEqu1).angularSize();

        var d2 = Epoch.J2010.daysUntil(zdt2);
        var eclToEqu2 = new EclipticToEquatorialConversion(zdt2);
        var angSz2 = SunModel.SUN.at(d2, eclToEqu2).angularSize();

        var d3 = Epoch.J2010.daysUntil(zdt3);
        var eclToEqu3 = new EclipticToEquatorialConversion(zdt3);
        var angSz3 = SunModel.SUN.at(d3, eclToEqu3).angularSize();

        var d4 = Epoch.J2010.daysUntil(zdt4);
        var eclToEqu4 = new EclipticToEquatorialConversion(zdt4);
        var angSz4 = SunModel.SUN.at(d4, eclToEqu4).angularSize();

        assertEquals(0.009170930832624435, angSz1, 1e-8);
        assertEquals(0.009321331046521664, angSz2, 1e-8);
        assertEquals(0.009462745860219002, angSz3, 1e-8);
        assertEquals(0.00932283978909254, angSz4, 1e-8);
    }

}
