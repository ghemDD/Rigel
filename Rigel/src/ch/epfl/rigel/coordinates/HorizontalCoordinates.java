package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;


import java.util.Locale;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.rigel.math.Angle;

/**
 * Horizontal Coordinates
 * @author Nael Ouerghemi
 *
 */
public final class HorizontalCoordinates extends SphericalCoordinates {

	private HorizontalCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}

	/**
	 * Creation of horizontal coordinates with the desired values in radians
	 * @param az : azimut (in radians)
	 * @param alt : altitude (in radians)
	 * @return horizontal coordinates with the desired values
	 */
	public static HorizontalCoordinates of(double az, double alt) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, az);

		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, alt);

		return new HorizontalCoordinates(az , alt);
	}

	/**
	 * Creation of horizontal coordinates with the desired values in degrees
	 * @param az : azimut (in degrees)
	 * @param alt : altitude (in degrees)
	 * @return horizontal coordinates with the desired values
	 */
	public static HorizontalCoordinates ofDeg(double az, double alt) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, az);

		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, alt);


		return new HorizontalCoordinates(Angle.ofDeg(az), Angle.ofDeg(alt));
	}

	/**
	 * Getter for the azimut (radians)
	 * @return value of the azimut
	 */
	public double az() {return super.lon();}

	/**
	 * Getter for the azimut (degrees)
	 * @return value of the azimut (in degrees)
	 */
	public double azDeg() {return super.lonDeg();}

	/**
	 * Getter for the altitude
	 * @return value of the altitude (in radians)
	 */
	public double alt() {return super.lat();}

	/**
	 * Getter for the altitude (in degrees)
	 * @return value of the altitude 'in degrees)
	 */
	public double altDeg() {return super.latDeg();}


	/**
	 * Construction of a string the polar direction given the horizontal coordinates
	 * @param n : 1st direction
	 * @param e : 2nd direction
	 * @param s : 3rd direction
	 * @param w : 4th direction
	 * @return String representing the polar direction given the horizontal coordinates
	 */
	public String azOctantName(String n, String e, String s, String w) {
		StringBuilder string=new StringBuilder();

		RightOpenInterval north1=RightOpenInterval.of(315, 360);
		ClosedInterval north2=ClosedInterval.of(0, 45);

		ClosedInterval south=ClosedInterval.of(135, 225);

		RightOpenInterval west=RightOpenInterval.of(202.5, 337.5);

		RightOpenInterval east=RightOpenInterval.of(22.5, 157.5);

		if (north1.contains(azDeg()) || north2.contains(azDeg()))
			string.append(n);

		if (south.contains(azDeg()))
			string.append(s);

		if (east.contains(azDeg()))
			string.append(e);

		if (west.contains(azDeg()))
			string.append(w);

		return string.toString();
	}

	/**
	 * Compute the angular distance between these and that horizontal coordinates
	 * @param that Horizontal Coordinates
	 * @return : angular distance between this and that
	 */
	public double angularDistanceTo(HorizontalCoordinates that) {

		double sinD=Math.sin(this.alt()) *Math.sin(that.alt());
		double cosD=Math.cos(this.alt()) * Math.cos(that.alt() );
		double delta=Math.cos(this.az() - that.az());

		return Math.acos(sinD + cosD*delta);
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", lonDeg(), latDeg());
	}
}
