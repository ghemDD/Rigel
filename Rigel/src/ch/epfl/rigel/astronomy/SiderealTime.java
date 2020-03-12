package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

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
		double millisPerHour=3600.0*1000.0;
		when=when.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime whenDayInstant=when.truncatedTo(ChronoUnit.DAYS);
		ZonedDateTime whenInstant=when.truncatedTo(ChronoUnit.NANOS);

		double T=Epoch.J2000.julianCenturiesUntil(whenDayInstant);
		
		double decimal=whenDayInstant.until(whenInstant, ChronoUnit.MILLIS);
		double t1=decimal/millisPerHour;
		
		Polynomial greenwich=Polynomial.of(0.000025862, 2400.051336, 6.697374558);
		double so=greenwich.at(T);
		
		
		double s1=1.002737909*t1;
		double sg=so+s1;
		
		return normalizePositive(ofHr(sg));
	}

	/**
	 * Compute local sidereal time at date when and location where
	 * @param when : date from which the local sidereal time is computed
	 * @param where : location from which the local sidereal time is computed
	 * @return local sidereal time at date when and location where
	 */
	public static double local(ZonedDateTime when, GeographicCoordinates where) {
		return normalizePositive(greenwich(when)+where.lon());
	}	
}
