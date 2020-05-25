package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

/**
 * Represents Ecliptic Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */

public final class EclipticCoordinates extends SphericalCoordinates {

	private EclipticCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of ecliptic coordinates with the desired values (in radians)
	 * 
	 * @param lon
	 * 			Longitude (in radians) 
	 * 
	 * @param lat
	 * 			Latitude (in radians)
	 * 
	 * @return equatorial coordinates with the desired longitude and latitude
	 */
	public static EclipticCoordinates of(double lon, double lat) {
		checkInInterval(LONGITUDE_INT, lon);
		checkInInterval(LATITUDE_INT, lat);

		return new EclipticCoordinates(lon, lat);
	}

	/**
	 * @see SphericalCoordinates#lon()
	 */
	@Override
	public double lon() {return super.lon();}

	/**
	 * Getter for longitude
	 * 
	 * @return longitude in degrees
	 */
	public double lonDeg() {return super.lonDeg();}

	/**
	 * @see SphericalCoordinates#lat()
	 */
	@Override
	public double lat() {return super.lat();}

	/**
	 * Getter for latitude
	 * 
	 * @return latitude in degree
	 */
	public double latDeg() {return super.latDeg();}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
	}
}
