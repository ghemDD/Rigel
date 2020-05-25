package ch.epfl.rigel.astronomy;

import java.time.LocalDate;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

/**
 * Enumeration using two dates from which temporal distance (in julian centuries/days) can be used for other computations
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum Epoch {

	J2000(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.NOON, ZoneId.of("UTC")),
	J2010(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.MIDNIGHT, ZoneId.of("UTC"));

	private final ZonedDateTime date;
	private static final double MILLIS_PER_SECOND = 1000.0;
	private static final double SECONDS_PER_MINUTS = 60.0;
	private static final double MINUTS_PER_HOUR = 60.0;
	private static final double HOURS_PER_DAY = 24.0;
	private static final double DAYS_PER_CENTURY = 36525.0;
	private static final double MILLIS_PER_DAY = MILLIS_PER_SECOND * SECONDS_PER_MINUTS * MINUTS_PER_HOUR * HOURS_PER_DAY;
	
	
	private Epoch(LocalDate d, LocalTime time, ZoneId zone) {
		date = ZonedDateTime.of(d, time, zone);
	}

	/**
	 * Compute the number of days from the chosen epoch (J2000/J2010) to the desired date when
	 * 
	 * @param when 
	 * 			date to which the number of days is calculated
	 * 
	 * @return number of days from the chosen epoch (J2000/J2010) to the desired date when
	 */ 
	public double daysUntil(ZonedDateTime when) {
		double n = (double) date.until(when, ChronoUnit.MILLIS);

		return n / (MILLIS_PER_DAY); 
	}

	/**
	 * Compute the number of julian centuries from the chosen epoch (J2000/J2010) to the desired date when
	 * 
	 * @param when 
	 * 			date to which the number of julian centuries is calculated
	 * 
	 * @return number of julian centuries from the chosen epoch (J2000/J2010) to the desired date when
	 */ 
	public double julianCenturiesUntil(ZonedDateTime when) {

		return daysUntil(when) / DAYS_PER_CENTURY; 
	}
}
