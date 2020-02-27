package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Ecliptic Coordinates
 * @author Nael Ouerghemi
 *
 */

public final class EclipticCoordinates extends SphericalCoordinates {

	private EclipticCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of ecliptic coordinates with the desired values in radians
	 * @param lon : longitude in radians 
	 * @param lat : latitude in radians
	 * @return Equatorial Coordinates with the desired longitude and latitude
	 */
	public static EclipticCoordinates of(double lon, double lat) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, lon);

		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, lat);

		return new EclipticCoordinates(lon, lat);
	}

	/**
	 * Creation of ecliptic coordinates with the desired values in degrees
	 * @param lon : longitude in degrees 
	 * @param lat : latitude in degrees
	 * @return Equatorial Coordinates with the desired longitude and latitude
	 */
	public static EclipticCoordinates ofDeg(double lon, double lat) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, lon);

		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, lat);

		return new EclipticCoordinates(Angle.ofDeg(lon), Angle.ofDeg(lat));
	}

	@Override
	public double lon() {return super.lon();}

	/**
	 * Getter for longitude
	 * @return longitude in degrees
	 */
	public double lonDeg() {return super.lonDeg();}

	@Override
	public double lat() {return super.lat();}

	/**
	 * Getter for latitude
	 * @return latitude in degree
	 */
	public double latDeg() {return super.latDeg();}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
	}
}
