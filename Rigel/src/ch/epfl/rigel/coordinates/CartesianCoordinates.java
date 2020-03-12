package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates {
	private double x;
	private double y;

	private CartesianCoordinates(double abs, double ord) {
		x=abs;
		y=ord;
	}

	public static CartesianCoordinates of(double x, double y) {
		return new CartesianCoordinates(x, y);
	}

	public double x() {return x;}

	public double y() {return y;}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", x(), y());
	}

	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}
