package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;
/**
 * A closed Interval
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class ClosedInterval extends Interval {

	private ClosedInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}

	/**
	 * Create a closed interval with respect to the two bounds given below
	 * 
	 * @param low
	 * 			Lower bound of the interval
	 *  
	 * @param up
	 * 			Upper bound of the interval
	 * 
	 * @return the interval [low, up] if the parameters given are correct (low>up);
	 */
	public static ClosedInterval of(double low, double up) {
		checkArgument(low<up);

		return new ClosedInterval(low, up);
	}

	/**
	 * Create a closed interval of length "size" centered on 0
	 * 
	 * @param size
	 * 			Size of the interval 
	 * 
	 * @return the interval [-size/2, size/2] if the parameters given are correct (size>0) 
	 */
	public static ClosedInterval symmetric(double size) {
		checkArgument(size>0);

		return new ClosedInterval(-size/2, size/2);
	}

	/**
	 * @see Interval#contains(double v)
	 */
	@Override
	public boolean contains(double v) {

		return (v>=low() && v<=high());
	}

	/**
	 * Clip function
	 * 
	 * @param v
	 * 			Antecedent
	 * 
	 * @return value of the clip function with respect to the value v and the current instance of the interval
	 */
	public double clip(double v) {

		if (v<=low())
			return low();

		else if (v>=high())
			return high();

		else
			return v;
	}

	/**
	 * @see Object#toString()
	 */
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "[%s,%s]", low(), high());	
	}
}
