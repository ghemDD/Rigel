package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.ClosedInterval;


import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import static ch.epfl.rigel.Preconditions.*;

import java.util.Locale;

/**
 * Geographic Coordinates
 * @author Nael Ouerghemi
 *
 */
public final class GeographicCoordinates extends SphericalCoordinates {
	final static RightOpenInterval LONGITUDE_INT=RightOpenInterval.symmetric(360);
	final static ClosedInterval LATITUDE_INT=ClosedInterval.symmetric(180);

	private GeographicCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of geographic coordinates with the desired values in degrees
	 * @param lonDeg : longitude in degrees
	 * @param latDeg : latitude in degrees
	 * @return geographic coordinates with the desired values
	 */
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
		checkArgument(isValidLonDeg(lonDeg));
		checkArgument(isValidLatDeg(latDeg));

		return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
	}

	/**
	 * Determine if an angle of value lonDeg (in degrees) is a valid longitude
	 * @param lonDeg : value of the angle to test in degrees
	 * @return True if the angle is a valid longitude/ False if not
	 */
	public static boolean isValidLonDeg(double lonDeg) {
		if (LONGITUDE_INT.contains(lonDeg))
			return true;

		return false;
	}

	/**
	 * Determine if an angle of value latDeg (in degrees) is a valid latitude
	 * @param latDeg : value of the angle to test in degrees
	 * @return True if the angle is a valid latitude/ False if not
	 */
	public static boolean isValidLatDeg(double latDeg) {
		if (LATITUDE_INT.contains(latDeg))
			return true;

		return false;
	}

	@Override
	public double lon() {return super.lon();}

	@Override
	public double lonDeg() {return super.lonDeg();}

	@Override
	public double lat() {return super.lat();}

	@Override
	public double latDeg() {return super.latDeg();}

	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
	}
}
