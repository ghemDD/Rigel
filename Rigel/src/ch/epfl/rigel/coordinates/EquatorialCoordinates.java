package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.util.Locale;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

/**
 * Equatorial Coordinates
 * @author Nael Ouerghemi / Tanguy Marbot
 *
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

	private EquatorialCoordinates(double ra, double dec) {
		super(ra, dec);
	}
	
	/**
	 * 
	 * @param ra
	 * @param dec
	 * @return
	 */
	public static EquatorialCoordinates of(double ra, double dec) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, ra);
		
		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, dec);
		
		return new EquatorialCoordinates(ra , dec);
	}
	
	/**
	 * 
	 * @param az
	 * @param alt
	 * @return
	 */
	public static EquatorialCoordinates ofDeg(double ra, double dec) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, ra);
		
		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, dec);
		
		return new EquatorialCoordinates(Angle.ofDeg(ra), Angle.ofDeg(dec));
	}
	
	/**
	 * 
	 * @return
	 */
	public double ra() {return super.lon();}
	
	/**
	 * 
	 * @return
	 */
	public double raDeg() {return super.lonDeg();}
	
	/**
	 * 
	 * @return
	 */
	public double raHr() {return Angle.toHr(ra());}
	
	/**
	 * 
	 * @return
	 */
	public double dec() {return super.lat();}
	
	/**
	 * 
	 * @return
	 */
	public double decDeg() {return super.latDeg();}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", Angle.toHr(lon()), latDeg());
	}
}
