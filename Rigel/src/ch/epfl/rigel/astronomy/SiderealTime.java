package ch.epfl.rigel.astronomy;

import java.time.ZoneId;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Polynomial;

/**
 * Sidereal Time : computation of the sidereal time of Greenwich depending on a certain date and local sidereal time 
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class SiderealTime {
	
	private static final double MILLIS_PER_HOUR = 3600.0 * 1000.0;
	private SiderealTime() {}
	
	/**
	 * Compute the sidereal time of Greenwich depending on the date when
	 * 
	 * @param when 
	 * 			date from which the sidereal time of Greenwich is computed
	 * 
	 * @return sidereal time of Greenwich at date when
	 */
	public static double greenwich(ZonedDateTime when) {
		when = when.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime whenDayInstant = when.truncatedTo(ChronoUnit.DAYS);
		ZonedDateTime whenInstant = when.truncatedTo(ChronoUnit.NANOS);

		double julian = Epoch.J2000.julianCenturiesUntil(whenDayInstant);

		//Number of millisSeconds (decimal) from the beginning of the day (whenDayInstant) to the hour of the day (whenDayInstant)
		double decimalTime = whenDayInstant.until(whenInstant, ChronoUnit.MILLIS);

		//Number of hours in decimal from the beginning of the day (whenDayInstant) to the hour of the day (whenDayInstant)
		double t1 = decimalTime/MILLIS_PER_HOUR;

		Polynomial greenwichSo = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
		double so = greenwichSo.at(julian);

		Polynomial greenwichSone = Polynomial.of(1.002737909, 0);
		double s1 =  greenwichSone.at(t1);

		double sg = so + s1;

		return normalizePositive(ofHr(sg));
	}

	/**
	 * Computes local sidereal time at date when and location where
	 * 
	 * @param when 
	 * 			date from which the local sidereal time is computed
	 * 
	 * @param where 
	 * 			location from which the local sidereal time is computed
	 * 
	 * @return local sidereal time at date when and location where
	 */
	public static double local(ZonedDateTime when, GeographicCoordinates where) {
		return normalizePositive(greenwich(when)+where.lon());
	}	
}
