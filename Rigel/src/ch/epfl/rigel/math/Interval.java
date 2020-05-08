package ch.epfl.rigel.math;

/**
 * Represents an Interval
 * 
 * @author Nael Ouerghemi (310435)
 */
public abstract class Interval {

	private final double lowerBound;
	private final double upperBound;

	protected Interval(double boundA, double boundB) {
		lowerBound = boundA;
		upperBound = boundB;
	}

	/**
	 * Getter for the lower bound of the interval
	 * 
	 * @return lower bound of the interval
	 */
	public double low() {return lowerBound;}

	/**
	 * Getter for the upper bound of the interval
	 * 
	 * @return upper bound of the interval
	 */
	public double high() {return upperBound;}

	/**
	 * Size of the interval
	 * 
	 * @return size of the interval
	 */
	public double size() {return upperBound-lowerBound;}

	/**
	 * Returns true if the value v is in interval, false otherwise
	 * 
	 * @param v
	 * 			value to be tested
	 * 
	 * @return true if the value v is in interval, false otherwise
	 */
	public abstract boolean contains(double v);

	/**
	 * @see Object#equals()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Object#hashCode()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException();
	}
}
