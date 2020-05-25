package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;


import java.util.Locale;

import ch.epfl.rigel.math.Angle;

/**
 * Equatorial Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

	private EquatorialCoordinates(double ra, double dec) {
		super(ra, dec);
	}

	/**
	 * Creation of equatorial coordinates with the desired values (in radians)
	 * 
	 * @param ra
	 * 			Right ascension (radians)
	 * 
	 * @param dec
	 * 			Declination (radian)
	 * 
	 * @return Equatorial Coordinates with the desired right ascension and declination
	 */
	public static EquatorialCoordinates of(double ra, double dec) {

		checkInInterval(LONGITUDE_INT, ra);
		checkInInterval(LATITUDE_INT, dec);

		return new EquatorialCoordinates(ra , dec);
	}

	/**
	 * Getter for the right ascension (radian)
	 * 
	 * @return right ascension (radian)
	 */
	public double ra() {return super.lon();}

	/**
	 * Getter for the right ascension (degree)
	 * 
	 * @return right ascension (degree)
	 */
	public double raDeg() {return super.lonDeg();}

	/**
	 * Getter for the right ascension (hour)
	 * 
	 * @return right ascension (hour)
	 */
	public double raHr() {return Angle.toHr(ra());}

	/**
	 * Getter for the declination (radian)
	 * 
	 * @return declination (radian)
	 */
	public double dec() {return super.lat();}

	/**
	 * Getter for the declination (degree)
	 * 
	 * @return declination (degree)
	 */
	public double decDeg() {return super.latDeg();}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
	}
}
