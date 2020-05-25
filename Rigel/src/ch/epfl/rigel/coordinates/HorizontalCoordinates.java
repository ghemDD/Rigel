package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;
import static java.lang.Math.cos;
import static java.lang.Math.acos;
import static java.lang.Math.sin;



import java.util.Locale;

import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.rigel.math.Angle;

/**
 * Horizontal Coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

	/**
	 * Intervals of azimuth defining the octants
	 */
	//Remove intervals
	private static final RightOpenInterval NORTH_1 = RightOpenInterval.of(292.5, 360);
	private static final RightOpenInterval NORTH_2 = RightOpenInterval.of(0, 67.5);
	private static final RightOpenInterval SOUTH = RightOpenInterval.of(112.5, 247.5);
	private static final RightOpenInterval WEST = RightOpenInterval.of(202.5, 337.5);
	private static final RightOpenInterval EAST = RightOpenInterval.of(22.5, 157.5);

	public static final String NORTH_STRING = "N";
	public static final String SOUTH_STRING = "S";
	public static final String EAST_STRING = "E";
	public static final String WEST_STRING = "O";


	private HorizontalCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of horizontal coordinates with the desired values in radians
	 * 
	 * @param az
	 * 			Azimuth (in radians)
	 * 
	 * @param alt
	 * 			Altitude (in radians)
	 * 
	 * @return horizontal coordinates with the desired values
	 */
	public static HorizontalCoordinates of(double az, double alt) {

		checkInInterval(LONGITUDE_INT, az);
		checkInInterval(LATITUDE_INT, alt);

		return new HorizontalCoordinates(az , alt);
	}

	/**
	 * Creation of horizontal coordinates with the desired values in degrees
	 * 
	 * @param azDeg
	 * 			Azimuth (in degrees)
	 * 
	 * @param altDeg
	 * 			Altitude (in degrees)
	 * 
	 * @return horizontal coordinates with the desired values
	 */
	public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {

		checkInInterval(LONGITUDE_INT_DEG, azDeg);
		checkInInterval(LATITUDE_INT_DEG, altDeg);

		return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
	}

	/**
	 * Construction of a string the polar direction given the horizontal coordinates
	 * 
	 * @param n
	 * 			1st direction
	 * 
	 * @param e
	 * 			2nd direction
	 * 
	 * @param s
	 * 			3rd direction
	 * 
	 * @param w
	 * 			4th direction
	 * 
	 * @return String representing the polar direction given the horizontal coordinates
	 */
	public String azOctantName(String n, String e, String s, String w) {
		StringBuilder string = new StringBuilder();

		if (NORTH_1.contains(azDeg()) || NORTH_2.contains(azDeg()))
			string.append(n);

		if (SOUTH.contains(azDeg()))
			string.append(s);

		if (EAST.contains(azDeg()))
			string.append(e);

		if (WEST.contains(azDeg()))
			string.append(w);

		return string.toString();
	}

	/**
	 * Compute the angular distance between these and that horizontal coordinates
	 * 
	 * @param that 
	 * 			Horizontal Coordinates of location that
	 * 
	 * @return angular distance between this and that
	 */
	public double angularDistanceTo(HorizontalCoordinates that) {

		double sinD = sin(this.alt()) * sin(that.alt());
		double cosD = cos(this.alt()) * cos(that.alt());
		double delta = cos(this.az() - that.az());

		return acos(sinD + cosD*delta);
	}

	/**
	 * Getter for the azimuth (radians)
	 * 
	 * @return value of the azimuth
	 */
	public double az() {return super.lon();}

	/**
	 * Getter for the azimuth (degrees)
	 * 
	 * @return value of the azimuth (in degrees)
	 */
	public double azDeg() {return super.lonDeg();}

	/**
	 * Getter for the altitude
	 * 
	 * @return value of the altitude (in radians)
	 */
	public double alt() {return super.lat();}

	/**
	 * Getter for the altitude (in degrees)
	 * 
	 * @return value of the altitude (in degrees)
	 */
	public double altDeg() {return super.latDeg();}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", lonDeg(), latDeg());
	}
}
