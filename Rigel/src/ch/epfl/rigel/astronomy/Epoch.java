package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public enum Epoch {

	J2000(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0), ZoneId.of("UTC")),
	J2010(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.of(0, 0), ZoneId.of("UTC"));
	
	private Epoch(LocalDate d, LocalTime time, ZoneId zone) {
		date=ZonedDateTime.of(d,time, zone);
	}
	
	private ZonedDateTime date;
	
	public double daysUntil(ZonedDateTime when) {
		double n=(double) date.until((Temporal) when, ChronoUnit.MILLIS);
		
		return n/(1000*60*60*24); 
	}
	
	public double julianCenturiesUntil(ZonedDateTime when) {
		return daysUntil(when)/36525; 
	}
}
