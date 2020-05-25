package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * Represents an angle
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class Angle {

	/**
	 * Constant TAU
	 */
	public final static double TAU = 2 * Math.PI;

	/**
	 * Number of degree per radian
	 */
	public static final double DEG_PER_RAD = 360.0 / TAU;

	/**
	 * Number of radians per degree
	 */
	public static final double RAD_PER_DEG= TAU / 360.0;

	/**
	 * Number of radians per hour
	 */
	public final static double RAD_PER_HR = ofDeg(15.0);

	/**
	 * Number of hour per radian
	 */
	public final static double HR_PER_RAD = 24 / TAU;

	/**
	 * Number of radians per minute
	 */
	public final static double RAD_PER_MIN = ofDeg(1.0/60.0);

	/**
	 * Number of seconds per minutes
	 */
	public final static int SEC_PER_MIN = 60;

	/**
	 * Number of minutes per degree
	 */
	public final static int MIN_PER_DEG = 60;

	/**
	 * Number of radians per second
	 */
	public final static double RAD_PER_SEC = ofDeg(1.0/3600.0);

	private static final RightOpenInterval NORMAL = RightOpenInterval.of(0,  TAU);

	private Angle() {}

	/**
	 * Normalization of the angle on the interval [O, TAU[
	 * 
	 * @param rad 
	 * 			Angle in radian to normalize
	 * 
	 * @return Normalization of the angle on the interval [O, TAU[
	 */
	public static double normalizePositive(double rad) {

		return NORMAL.reduce(rad);
	}

	/**
	 * Conversion from arcsec to radians
	 * 
	 * @param sec
	 * 			Value of the angle in arcsec
	 * 
	 * @return value of the angle in radians
	 */
	public static double ofArcsec(double sec) {
		return sec * RAD_PER_SEC;
	}

	/**
	 * Conversion from deg/minutes/arcsecs to radians
	 * 
	 * @param deg 
	 * 			Number of degrees of the angle
	 * 
	 * @param min
	 * 			Number of arcminutes of the angle
	 *  
	 * @param sec
	 * 			Number of arcsec of the angle
	 * 	
	 * @return	value of the angle in radians
	 */
	public static double ofDMS(int deg, int min, double sec) {
		checkArgument(deg>=0);
		checkArgument((0<=min && min<MIN_PER_DEG) && (0<=sec && sec<SEC_PER_MIN));

		return ofDeg(deg) + min*RAD_PER_MIN + ofArcsec(sec);
	}

	/**
	 * Conversion from degrees to radians
	 * 
	 * @param deg
	 * 			Value of the angle in degrees
	 * 
	 * @return	value of the angle in radians
	 */
	public static double ofDeg(double deg) {
		return deg * RAD_PER_DEG;
	}

	/**
	 * Conversion from radians to degrees
	 * 
	 * @param rad
	 * 			Value of the angle in radians
	 * 
	 * @return	value of the angle in degrees
	 */
	public static double toDeg(double rad) {
		return rad * DEG_PER_RAD;
	}

	/**
	 * Conversion from hours to radians
	 * 
	 * @param hr
	 * 			Value of the angle in hours
	 * 
	 * @return	value of the angle in radians
	 */
	public static double ofHr(double hr) {
		return hr * RAD_PER_HR;
	}

	/**
	 * Conversion from radians to Hours
	 * 
	 * @param rad 
	 * 			Value of the angle in radians
	 * 
	 * @return	value of the angle in hours
	 */
	public static double toHr(double rad) {
		return rad * HR_PER_RAD;
	}
}
