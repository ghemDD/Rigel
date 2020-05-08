package ch.epfl.rigel.coordinates;

import java.util.Locale;
/**
 * Represents Cartesian Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class CartesianCoordinates {
	private double x;
	private double y;

	private CartesianCoordinates(double abs, double ord) {
		x = abs;
		y = ord;
	}

	/**
	 * Construction method of CartesianCoordinates
	 * 
	 * @param x 
	 * 			abscissa
	 * @param y 
	 * 			ordinate
	 * 
	 * @return CartesianCoordinates corresponding to the given attributes x and y
	 */
	public static CartesianCoordinates of(double x, double y) {
		return new CartesianCoordinates(x, y);
	}

	/**
	 * Getter for abscissa x
	 * 
	 * @return x abscissa
	 */
	public double x() {return x;}

	/**
	 * Getter for ordinate y
	 * 
	 * @return y ordinate
	 */
	public double y() {return y;}
	
	/**
	 * 
	 * @param coor
	 * @return
	 */
	public double distance(CartesianCoordinates coor) {
		return 0;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", x(), y());
	}

	/**
	 * @see Object#equals()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Object#hashCode()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}
