package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * An angle
 * @author Nael Ouerghemi/Tanguy Marbot
 *
 */
public final class Angle {

	
	public final static double TAU=2*Math.PI;
	public static final double DEG_PER_RAD = 360.0 / TAU;
	public static final double RAD_PER_DEG= TAU / 360.0;
	
	public final static double RAD_PER_HR=ofDeg(15.0);
	public final static double RAD_PER_MIN=ofDeg(1.0/60.0);
	public final static double RAD_PER_SEC=ofDeg(1.0/3600.0);
	
	
	/**
	 * Normalization of the angle on the interval [O, TAU[
	 * @param rad : angle in radian to normalize
	 * @return Normalization of the angle on the interval [O, TAU[
	 */
	public static double normalizePositive(double rad) {
		RightOpenInterval normal=RightOpenInterval.of(0,  TAU);
		return normal.reduce(rad);
	}
	
	/**
	 * Conversion from arcsec to radians
	 * @param sec : value of the angle in arcsec
	 * @return : value of the angle in radians
	 */
	public static double ofArcsec(double sec) {
		return sec*RAD_PER_SEC;
	}
	
	/**
	 * Conversion from deg/minutes/arcsecs to radians
	 * @param deg
	 * @param min
	 * @param sec
	 * @return : value of the angle in radians
	 */
	public static double ofDMS(int deg, int min, double sec) {
		checkArgument((0<=min && min<60) && (0<=sec && sec<60));
		
		return ofDeg(deg)+min*RAD_PER_MIN+ofArcsec(sec);
	}
	
	/**
	 * Conversion from degrees to radians
	 * @param deg : value of the angle in degrees
	 * @return : value of the angle in degrees
	 */
	public static double ofDeg(double deg) {
		  return deg * RAD_PER_DEG;
		}
	
	/**
	 * Conversion from radians to degrees
	 * @param rad : value of the angle in radians
	 * @return : value of the angle in degrees
	 */
	public static double toDeg(double rad) {
		  return rad * DEG_PER_RAD;
		}
	
	/**
	 * Conversion from hours to radians
	 * @param hr : value of the angle in hours
	 * @return : value of the angle in radians
	 */
	public static double ofHr(double hr) {
		return (hr * RAD_PER_HR);
	}
	
	/**
	 * Conversion from radians to Hours
	 * @param rad : value of the angle in radians
	 * @return : value of the angle in hours
	 */
	public static double toHr(double rad) {
		return rad / RAD_PER_HR;
	}
}
