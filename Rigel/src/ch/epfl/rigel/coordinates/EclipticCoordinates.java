package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Ecliptic Coordinates
 * @author Nael Ouerghemi / Tanguy Marbot
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
	public static EclipticCoordinates of(double ra, double dec) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, ra);
		
		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, dec);
		
		return new EclipticCoordinates(ra , dec);
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
		return String.format("(az=%s°, alt=%s°)", lonDeg(), latDeg());
	}

}
