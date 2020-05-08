package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static ch.epfl.rigel.math.Angle.TAU;
import static ch.epfl.rigel.math.Angle.normalizePositive;
import static ch.epfl.rigel.math.Angle.ofDeg;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Modeling of the position of the sun
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum SunModel implements CelestialObjectModel<Sun>{

	SUN;

	private SunModel() {}

	private static final double LON_EPOCH = ofDeg(279.557208);
	private static final double PERI = ofDeg(283.112438);
	private static final double E = 0.016705;
	private static final double ANGULAR = ofDeg(0.533128);

	/**
	 * Returns a representation of the sun at a given time depending on the epoch J2010 and location
	 * 
	 * @param daysSinceJ2010
	 * 				Number of days between the epoch J2010 and wanted time
	 * 
	 * @param eclipticToEquatorialConversion
	 * 				Conversion from ecliptic to equatorial coordinates used
	 * 
	 * @return representation of the sun at a given time depending on the epoch J2010 and location
	 */
	@Override
	public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

		double meanAnomaly = meanAnomaly(daysSinceJ2010);

		double realAnomaly = realAnomaly(meanAnomaly); 

		double longitude = longitude(realAnomaly);

		double angularSize = angularSize(realAnomaly); 

		EclipticCoordinates ecl = of(longitude, 0);
		EquatorialCoordinates equ = eclipticToEquatorialConversion.apply(ecl);

		return new Sun(ecl, equ, (float) angularSize, (float) meanAnomaly);
	}

	/**
	 * Returns mean anomaly of the Sun
	 * 
	 * @param daysSinceJ2010
	 * 			Number of days since epoch J2010 
	 * 
	 * @return mean anomaly at given time
	 */
	private double meanAnomaly(double daysSinceJ2010) {

		return (TAU/365.242191)*daysSinceJ2010 + LON_EPOCH - PERI;
	}

	/**
	 * Returns real anomaly of the Sun given its mean anomaly
	 * 
	 * @param mean
	 * 			Mean anomaly of the Sun
	 * 
	 * @return real anomaly of the Sun
	 */
	private double realAnomaly(double mean) {

		return mean + 2*E*sin(mean);
	}

	/**
	 * Returns ecliptic longitude of the sun 
	 * 
	 * @param realAnomaly
	 * 			Real anomaly of the Sun
	 * 
	 * @return ecliptic longitude of the sun
	 */
	private double longitude(double realAnomaly) {

		return normalizePositive(realAnomaly + PERI);
	}

	/**
	 * Returns angular size of the sun
	 * 
	 * @param realAnomaly
	 * 			Real anomaly of the Sun
	 * 
	 * @return angular size of the Sun
	 */
	private double angularSize(double realAnomaly) {

		return ANGULAR * (1 + E*cos(realAnomaly))/(1 - E*E);
	}
}
