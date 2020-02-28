package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

/**
 * Sideral Time : computation of the greenwich sidereal time depending on a certain date and local sidereal time depending on a certain location
 * @author Nael Ouerghemi
 *
 */
public final class SiderealTime {

	private SiderealTime() {}

	/**
	 * Compute the greenwich sidereal time depending on the date when
	 * @param when : date from which the greenwich sidereal time is computed
	 * @return greenwich sidereal time at date when
	 */
	public static double greenwich(ZonedDateTime when) {
		when=when.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime whenDayInstant=when.truncatedTo(ChronoUnit.DAYS);
		ZonedDateTime whenInstant=when.truncatedTo(ChronoUnit.MILLIS);


		double T=Epoch.J2000.julianCenturiesUntil(whenDayInstant);
		double t=(double) whenInstant.getHour() + whenInstant.getMinute()/60.0 + whenInstant.getSecond()/3600.0;
		
		System.out.println("Valeur de t:"+t);
		System.out.println("Valeur de T:"+T);
		
		Polynomial greenwich=Polynomial.of(0.000025862, 2400.051336, 6.697374558);
		System.out.println(greenwich);
		double so=greenwich.at(T);
		System.out.println("Valeur de So :"+so);
		double s1=1.002737909*t;
		System.out.println("Valeur de S1 :"+s1);

		double sg=so+s1;
		System.out.println("Valeur de Sg :"+sg);

		double sg1= sg - Math.floor(sg/24) * 24;
		System.out.println("Valeur de sg normalisée 2 :"+ofHr(sg1));
		System.out.println("Valeur de sg normalisée 1 :"+normalizePositive(ofHr(sg)));
		return ofHr(sg1); 
	}

	/**
	 * Compute local sidereal time at date when and location where
	 * @param when : date from which the local sidereal time is computed
	 * @param where : location from which the local sidereal time is computed
	 * @return local sidereal time at date when and location where
	 */
	public static double local(ZonedDateTime when, GeographicCoordinates where) {

		return greenwich(when)+where.lon();
	}	
}
