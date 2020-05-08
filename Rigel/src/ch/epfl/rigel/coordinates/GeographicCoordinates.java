package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import static ch.epfl.rigel.Preconditions.*;

import java.util.Locale;

/**
 * Geographic Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 *
 */
public final class GeographicCoordinates extends SphericalCoordinates {

	private static final RightOpenInterval GEO_LONGITUDE_INT_DEG = RightOpenInterval.of(-180, 180);

	private GeographicCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of geographic coordinates with the desired values in degrees
	 * 
	 * @param lonDeg
	 * 			Longitude in degrees
	 * 
	 * @param latDeg
	 * 			Latitude in degrees
	 * 
	 * @throws IllegalArgumentException
	 *			lonDeg/latDeg is not in the interval LONGITUDE_INT_DEG/LATITUDE_INT_DEG
	 * 			
	 * @return geographic coordinates with the desired values
	 */
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
		checkArgument(isValidLonDeg(lonDeg));
		checkArgument(isValidLatDeg(latDeg));

		return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
	}

	/**
	 * Determine if an angle of value lonDeg (in degrees) is a valid longitude
	 * 
	 * @param lonDeg
	 * 			Value of the angle to test in degrees
	 * 
	 * @return True if the angle is a valid longitude
	 * 		   False if not
	 */
	public static boolean isValidLonDeg(double lonDeg) {

		return GEO_LONGITUDE_INT_DEG.contains(lonDeg);
	}

	/**
	 * Determine if an angle of value latDeg (in degrees) is a valid latitude
	 * 
	 * @param latDeg
	 * 			Value of the angle to test in degrees
	 * 
	 * @return True if the angle is a valid latitude 
	 * 		   False otherwise
	 */
	public static boolean isValidLatDeg(double latDeg) {

		return LATITUDE_INT_DEG.contains(latDeg);
	}

	/**
	 * @see SphericalCoordinates#lon()
	 */
	@Override
	public double lon() {return super.lon();}

	/**
	 * @see SphericalCoordinates#lonDeg()
	 */
	@Override
	public double lonDeg() {return super.lonDeg();}

	/**
	 * @see SphericalCoordinates#lat()
	 */
	@Override
	public double lat() {return super.lat();}

	/**
	 * @see SphericalCoordinates#latDeg()
	 */
	@Override
	public double latDeg() {return super.latDeg();}

	/**
	 * @see Object#toString()
	 */
	@Override 
	public String toString() {
		return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
	}
}
