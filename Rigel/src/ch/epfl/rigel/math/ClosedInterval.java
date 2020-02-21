package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;
/**
 * A closed Interval
 * @author Nael Ouerghemi/Tanguy Marbot
 *
 */
public final class ClosedInterval extends Interval {

	private ClosedInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}
	
	/**
	 * Create a closed interval with respect to the two bounds given below
	 * @param low : lower bound of the interval 
	 * @param up : upper bound of the interval
	 * @return the interval [low, up] if the parameters given are correct (low>up);
	 */
	
	public static ClosedInterval of(double low, double up) {
		checkArgument(low<up);
		
		return new ClosedInterval(low, up);
	}
	
	/**
	 * Create a closed interval of length "size" centered on 0
	 * @param size : size of the interval 
	 * @return the interval [-size/2, size/2] if the parameters given are correct (size>0) 
	 */
	
	public static ClosedInterval symmetric(double size) {
		checkArgument(size>0);

		return new ClosedInterval(-size/2, size/2);
	}

	@Override
	public boolean contains(double v) {
		// TODO Auto-generated method stub
		if (v>=low() && v<=high())
			return true;
		
		return false;
	}
	
	/**
	 * Clip function
	 * @param v : antecedent
	 * @return Value of the clip function with respect to the value v and the current instance of the interval
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
	 * Override of the function toString of Object
	 * Return the representation string of the interval [lower, upper]
	 */
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "[%s,%s]", low(), high());	
	}
}
