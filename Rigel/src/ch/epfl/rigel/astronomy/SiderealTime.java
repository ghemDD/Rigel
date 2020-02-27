package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

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
		ZonedDateTime whenInstant=when.truncatedTo(ChronoUnit.DAYS);


		double T=Epoch.J2000.julianCenturiesUntil(whenDayInstant);
		double t=whenInstant.getHour();

		double so=0.000025862*T*T+2400.051336*T+6.697374558;
		double s1=1.002737909*t;

		double sg=so+s1;

		sg=ofHr(sg);

		return normalizePositive(sg); 
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
