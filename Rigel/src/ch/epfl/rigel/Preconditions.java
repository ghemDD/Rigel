package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;


public final class Preconditions {
	private Preconditions() {}

	public static void checkArgument(boolean isTrue) {
		if (!isTrue)
			throw new IllegalArgumentException();
	}

	public static double checkInInterval(Interval interval, double value) {

		if (interval.contains(value))
			return value;

		else {
			throw new IllegalArgumentException();
		}
	} 
}
