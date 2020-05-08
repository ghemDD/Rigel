package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;
/**
 * Represents a right open interval
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class RightOpenInterval extends Interval {

	private RightOpenInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}

	/**
	 Create a right open interval with respect to the two bounds given below

	 * @param low
	 * 			Lower bound of the interval
	 * 
	 * @param up
	 * 			Upper bound of the interval
	 * 
	 * @return the interval [low, up[ if the parameters given are correct (low>up);
	 */
	public static RightOpenInterval of(double low, double high) {
		checkArgument(low < high);

		return new RightOpenInterval(low, high);
	}

	/**
	 * Create a closed interval of length "size" centered on 0
	 * 
	 * @param size
	 * 			Size of the interval 
	 * 
	 * @throws IllegalArgumentException
	 * 			if size is negative or null
	 * 
	 * @return the interval [-size/2, size/2[ if the parameters given are correct (size>0) 
	 */
	public static RightOpenInterval symmetric(double size) {
		checkArgument(size > 0);

		return new RightOpenInterval(-size/2, size/2);
	}

	/**
	 * @see Interval#contains()
	 */
	@Override
	public boolean contains(double v) {

		return (v>=low() && v<high());
	}

	/**
	 * Reduce function
	 * 
	 * @param v
	 * 			Antecedent
	 * 
	 * @return value of the reduce function with respect to the value v and the current instance of the interval
	 */
	public double reduce(double v) {
		double a = low();
		double b = high();
		double floorMod = (v-a) - (b-a)*Math.floor((v-a) / (b-a));

		return  a + floorMod;
	}

	/**
	 * @see Object#toString()
	 */
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "[%s,%s[", low(), high());	
	}
}
