package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.*;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Spherical Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */
abstract class SphericalCoordinates {

	private double lon;
	private double lat;
	static final RightOpenInterval LONGITUDE_INT = RightOpenInterval.of(0, Angle.TAU);
	static final RightOpenInterval LONGITUDE_INT_DEG = RightOpenInterval.of(0, 360);
	static final ClosedInterval LATITUDE_INT = ClosedInterval.symmetric(Math.PI);
	static final ClosedInterval LATITUDE_INT_DEG = ClosedInterval.symmetric(180);

	SphericalCoordinates(double longitude, double latitude) {
		lon = longitude;
		lat = latitude;
	}

	double lon() {return lon;}

	double lonDeg() {return toDeg(lon);}

	double lat() {return lat;}

	double latDeg() {return toDeg(lat);}

	/**
	 * @see Object#equals()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Object#hashCode()
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException();
	}

}
