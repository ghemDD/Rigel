package ch.epfl.rigel.astronomy;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;

public final class SiderealTime {

	private SiderealTime() {}

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

	public static double local(ZonedDateTime when, GeographicCoordinates where) {

		return greenwich(when)+where.lon();
	}	
}
