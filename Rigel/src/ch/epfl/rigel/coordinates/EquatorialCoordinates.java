package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Equatorial Coordinates
 * @author Nael Ouerghemi
 *
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

	private EquatorialCoordinates(double ra, double dec) {
		super(ra, dec);
	}

	/**
	 * Creation of equatorial coordinates with the desired values in Radians
	 * @param ra : right ascension (radians)
	 * @param dec : declination (radian)
	 * @return Equatorial Coordinates with the desired right ascension and declination
	 */
	public static EquatorialCoordinates of(double ra, double dec) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, ra);

		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, dec);

		return new EquatorialCoordinates(ra , dec);
	}

	/**
	 * Creation of equatorial coordinates with the desired values in degree
	 * @param ra : right ascension (degrees)
	 * @param dec : declination (degrees)
	 * @return Equatorial Coordinates with the desired right ascension and dec (degrees)
	 */
	public static EquatorialCoordinates ofDeg(double ra, double dec) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, ra);

		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, dec);

		return new EquatorialCoordinates(Angle.ofDeg(ra), Angle.ofDeg(dec));
	}

	/**
	 * Getter for the right ascension (radian)
	 * @return right ascension (radian)
	 */
	public double ra() {return super.lon();}

	/**
	 * Getter for the right ascension (degree)
	 * @return right ascension (degree)
	 */
	public double raDeg() {return super.lonDeg();}

	/**
	 * Getter for the right ascension (hour)
	 * @return right ascension (hour)
	 */
	public double raHr() {return Angle.toHr(ra());}

	/**
	 * Getter for the declination (radian)
	 * @return declination (radian)
	 */
	public double dec() {return super.lat();}

	/**
	 * Getter for the declination (degree)
	 * @return declination (degree)
	 */
	public double decDeg() {return super.latDeg();}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
	}
}
