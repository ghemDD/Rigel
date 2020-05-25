package ch.epfl.rigel.coordinates;

import java.util.Locale;
import static java.lang.Math.sqrt;
/**
 * Represents Cartesian Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class CartesianCoordinates {
	private final double x;
	private final double y;

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
	 * Computes the cartesian distance between this coordinates and coor coordinates
	 * 
	 * @param coor
	 * 			Target of computation
	 * 
	 * @return cartesian distance between this coordinates and coor coordinates
	 */
	public double distance(CartesianCoordinates coor) {
		double distance = (x() - coor.x())*(x() - coor.x()) + (y() - coor.y())*(y() - coor.y());

		return sqrt(distance);
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
	 */
	@Override
	public final boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException();
	}
}
