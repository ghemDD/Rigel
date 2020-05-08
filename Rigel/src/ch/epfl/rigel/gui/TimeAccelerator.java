package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Represents an time accelerator 
 * 
 * @author Nael Ouerghemi (310435)
 */

@FunctionalInterface
public interface TimeAccelerator {

	/**
	 * Computes time acceleration
	 * 
	 * @param initialTime
	 * 			ZonedDateTime
	 * 			
	 * @param realTime
	 * 			in nanoseconds
	 * 
	 * @return
	 */
	public abstract ZonedDateTime adjust(ZonedDateTime initialTime, long realTime);

	public static TimeAccelerator continuous(int factor) {

		return (initialTime, realTime) -> initialTime.plusNanos(factor * realTime);
	}

	public static TimeAccelerator discrete(double freq, Duration step) {

		return (initialTime, realTime) -> initialTime.plusNanos( ((long) Math.floor(freq * realTime/1e9)) * step.toNanos());
	}
}
