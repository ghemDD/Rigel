package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.normalizePositive;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.util.Locale;
import java.util.function.Function;

/**
 * Represents a stereographic projection 
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

	private final HorizontalCoordinates center;
	private final double cosAltCenter;
	private final double sinAltCenter;

	/**
	 * Constructor of StereographicProjection
	 *  
	 * @param center
	 * 			Horizontal Coordinates of the point on which the projection is centered
	 */
	public StereographicProjection(HorizontalCoordinates center) {
		this.center = center;
		cosAltCenter = cos(center.alt());
		sinAltCenter = sin(center.alt());
	}

	/**
	 * Returns the coordinates of the projected point in Cartesian Coordinates
	 * 
	 * @param t
	 * 			Horizontal Coordinates of the original point
	 * 
	 * @return coordinates of the projected point in Cartesian Coordinates
	 */
	@Override
	public CartesianCoordinates apply(HorizontalCoordinates t) {
		double sinAltCoor = sin(t.alt());
		double cosAltCoor = cos(t.alt());

		double delta = t.az() - center.az();
		double cosDelta = cos(delta);
		double d = 1 / (1 + sinAltCenter*sinAltCoor + cosAltCenter*cosAltCoor*cosDelta);

		double x = d * cosAltCoor * sin(delta);
		double y = d * (cosAltCenter*sinAltCoor - sinAltCenter*cosAltCoor*cosDelta);

		return CartesianCoordinates.of(x, y);
	}

	/**
	 * Returns circle center coordinates corresponding to the projection of the parallel passing through the center
	 *  
	 * @param hor
	 * 			Horizontal Coordinates through which the parallel is passing
	 * 
	 * @return Cartesian Coordinates of the circle center
	 */
	public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
		double den = sinAltCenter + sin(hor.alt());

		return CartesianCoordinates.of(0, cosAltCenter/den);
	}

	/**
	 * Returns circle radius coordinates corresponding to the projection of the parallel passing through the center
	 *   
	 * @param parallel
	 * 			Horizontal Coordinates through which the parallel is passing
	 * 
	 * @return radius of the circle
	 */
	public double circleRadiusForParallel(HorizontalCoordinates parallel) {
		double den = sinAltCenter + sin(parallel.alt());
		double p = cos(parallel.alt())/den;

		return p;
	}

	/**
	 * Returns projected diameter given angular size
	 * 
	 * @param rad
	 * 			Angular size (radians)
	 * 
	 * @return projected diameter
	 */
	public double applyToAngle(double rad) {

		return 2 * tan(rad/4.0);
	}

	/**
	 * Returns the HorizontalCoordinates given the Cartesian Coordinates of the projected point
	 * 
	 * @param xy
	 * 			CartesianCoordinates of the projected point
	 * 
	 * @return original horizontal coordinates 
	 */
	public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {

		double delta, phi;

		if (xy.x() == 0 && xy.y()==0) {
			delta = center.az();
			phi = sinAltCenter;
		}

		else {
			double x = xy.x();
			double y = xy.y();

			double p = sqrt(x*x + y*y);
			double pSquared = p * p;

			double sinC = 2*p / (pSquared + 1);
			double cosC = (1 - pSquared) / (pSquared + 1);

			double numdelta = x * sinC;
			double dendelta = p*cosAltCenter*cosC - y*sinAltCenter*sinC;
			delta = atan2(numdelta, dendelta) + center.az();
			delta = normalizePositive(delta);

			double termA = cosC * sinAltCenter;
			double termB = (y * sinC * cosAltCenter) / p;
			phi = asin(termA + termB);
		}

		return HorizontalCoordinates.of(delta, phi);
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "StereographicProjection Center coordinates "+center.toString());
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
