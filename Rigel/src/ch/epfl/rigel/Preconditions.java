package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;
/**
 * Represents preconditions
 * 
 * @author Nael Ouerghemi (310435)
 *
 */
public final class Preconditions {
	private Preconditions() {}

	/**
	 * Checks if an argument is valid 
	 * 
	 * @param isTrue
	 * 			Condition to be checked
	 * 
	 * @throws IllegalArgumentException
	 * 			if the argument to be checked is false
	 * 
	 */
	public static void checkArgument(boolean isTrue) {
		if (!isTrue)
			throw new IllegalArgumentException("Argument not checked");
	}

	/**
	 * Checks if a value is contained in the specified interval
	 * 
	 * @param interval
	 * 			Interval in which the value has to be contained in
	 * 
	 * @param value
	 * 			Value to test
	 * 
	 * @return value
	 */
	public static double checkInInterval(Interval interval, double value) {

		if (interval.contains(value))
			return value;

		else {
			throw new IllegalArgumentException("Value not in interval");
		}
	} 
}
