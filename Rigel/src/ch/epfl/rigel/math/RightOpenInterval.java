package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

public final class RightOpenInterval extends Interval {

	private RightOpenInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}
	
	public static RightOpenInterval of(double low, double high) {
		checkArgument(low>=high);
		
		return new RightOpenInterval(low, high);
	}
	
	public static RightOpenInterval symmetric(double size) {
		checkArgument(size>=0);

		return new RightOpenInterval(-size, size);
	}

	@Override
	public boolean contains(double v) {
		// TODO Auto-generated method stub
		if (v>=getLower() && v<getUpper())
			return true;
		
		return false;
	}
	
	public double reduce(double v) {
		double a=getLower();
		double b=getUpper();
		double floorMod=(v-a)-(b-a)*Math.floor(v-a/b-a);
		
		return a+floorMod;
	}
	
	@Override
	public String toString() {
		return null;
	}
}
