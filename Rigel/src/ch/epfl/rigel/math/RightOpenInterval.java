package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;
/**
 * A right open interval
 * @author Nael Ouerghemi/Tanguy Marbot
 *
 */
public final class RightOpenInterval extends Interval {

	private RightOpenInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}
	
	/**
	 Create a right open interval with respect to the two bounds given below
	 * @param low : lower bound of the interval 
	 * @param up : upper bound of the interval
	 * @return the interval [low, up[ if the parameters given are correct (low>up);
	 */
	public static RightOpenInterval of(double low, double high) {
		checkArgument(low<high);
		
		return new RightOpenInterval(low, high);
	}
	
	/**
	 * Create a closed interval of length "size" centered on 0
	 * @param size : size of the interval 
	 * @return the interval [-size/2, size/2[ if the parameters given are correct (size>0) 
	 */
	public static RightOpenInterval symmetric(double size) {
		checkArgument(size>0);

		return new RightOpenInterval(-size/2, size/2);
	}

	@Override
	public boolean contains(double v) {
		// TODO Auto-generated method stub
		if (v>=getLower() && v<getUpper())
			return true;
		
		return false;
	}
	
	/**
	 * Reduce function
	 * @param v : antecedent
	 * @return Value of the reduce function with respect to the value v and the current instance of the interval
	 */
	public double reduce(double v) {
		double a=getLower();
		double b=getUpper();
		double floorMod=(v-a)-(b-a)*Math.floor((v-a)/(b-a));
		
		return a+floorMod;
	}
	
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "[%s,%s[", getLower(), getUpper());	
	}
}
