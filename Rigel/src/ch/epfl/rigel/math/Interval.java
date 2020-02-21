package ch.epfl.rigel.math;

public abstract class Interval {

	private final double bound1;
	private final double bound2;
	
	protected Interval(double boundA, double boundB) {
		bound1=boundA;
		bound2=boundB;
	}
	
	public double low() {return bound1;}
	
	public double high() {return bound2;}
	
	public double size() {return bound2-bound1;}
	
	public abstract boolean contains(double v);
	
	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}
