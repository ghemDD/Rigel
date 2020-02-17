package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

public final class ClosedInterval extends Interval {

	private ClosedInterval(double boundA, double boundB) {
		super(boundA, boundB);
	}
	
	public static ClosedInterval of(double low, double high) {
		checkArgument(low<high);
		
		return new ClosedInterval(low, high);
	}
	
	public static ClosedInterval symmetric(double size) {
		checkArgument(size>0);

		return new ClosedInterval(-size/2, size/2);
	}

	@Override
	public boolean contains(double v) {
		// TODO Auto-generated method stub
		if (v>=getLower() && v<=getUpper())
			return true;
		
		return false;
	}
	
	public double clip(double v) {
		
		if (v<=getLower())
			return getLower();
		
		else if (v>=getUpper())
			return getUpper();
		
		else
			return v;
	}
	
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "[%s,%s]", getLower(), getUpper());	
	}
}
