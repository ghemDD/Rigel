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
	 * 
	 * @param ra
	 * @param dec
	 * @return
	 */
	public static EclipticCoordinates of(double lon, double lat) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, lon);

		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, lat);

		return new EclipticCoordinates(lon, lat);
	}

	public static EclipticCoordinates ofDeg(double lon, double lat) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, lon);

		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, lat);

		return new EclipticCoordinates(Angle.ofDeg(lon), Angle.ofDeg(lat));
	}

	/**
	 * 
	 */
	public double lon() {return super.lon();}

	/**
	 * 
	 */
	public double lonDeg() {return super.lonDeg();}

	/**
	 * 
	 */
	public double lat() {return super.lat();}

	/**
	 * 
	 */
	public double latDeg() {return super.latDeg();}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
	}
}
