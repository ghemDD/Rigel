package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.log10;
import static java.lang.Math.tan;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import static ch.epfl.rigel.math.Angle.*;

import java.util.Arrays;
import java.util.List;

/**
 * Modeling of a planet location
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum PlanetModel implements CelestialObjectModel<Planet>{

	MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
			0.387098, 7.0051, 48.449, 6.74, -0.42),
	VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
			0.723329, 3.3947, 76.769, 16.92, -4.40),
	EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
			0.999985, 0, 0, 0, 0),
	MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
			1.523689, 1.8497, 49.632, 9.36, -1.52),
	JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
			5.20278, 1.3035, 100.595, 196.74, -9.40),
	SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
			9.51134, 2.4873, 113.752, 165.60, -8.88),
	URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
			19.21814, 0.773059, 73.926961, 65.80, -7.19),
	NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
			30.1985, 1.7673, 131.879, 62.20, -6.87);

	/**
	 * Immutable list of all planets used to compute their positions
	 */
	public final static List<PlanetModel> ALL=List.copyOf(Arrays.asList(PlanetModel.values()));
	private final String name;
	private final double lonEpoch;
	private final double peri;
	private final double exc;
	private final double half;
	private final double incli;
	private final double node;
	private final double magnitude;
	private final double cosIncli, sinIncli;
	private final double arcAngularSize;
	private final double termAnomaly;
	private final double excSquared;

	private PlanetModel(String name, double revPeriod, double lonEpoch, double peri, double exc, double half, double incli, double node, double angularSize, double magnitude) {
		this.name = name;
		this.lonEpoch = ofDeg(lonEpoch);
		this.peri = ofDeg(peri);
		this.exc = exc;
		this.half = half;
		this.incli = ofDeg(incli);
		this.node = ofDeg(node);
		this.magnitude = magnitude;
		cosIncli = cos(this.incli);
		sinIncli = sin(this.incli);
		arcAngularSize = ofArcsec(angularSize);
		termAnomaly = (TAU/365.242191) / revPeriod;
		excSquared = exc * exc;
	}

	/**
	 * Returns a representation of the planet at a given time depending on the epoch J2010 and location
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
	public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

		double meanAnomaly = this.meanAnomaly(daysSinceJ2010);
		double realAnomaly = this.realAnomaly(meanAnomaly);
		double r = this.radius(realAnomaly);
		double lonHelio = this.longitudeHelio(realAnomaly);

		//Previous variables adapted for the Earth : meanAnomaly, realAnomaly, radius, heliocentric longitude
		double meanAnomalyEarth = EARTH.meanAnomaly(daysSinceJ2010);
		double realAnomalyEarth = EARTH.realAnomaly(meanAnomalyEarth);
		double rEarth = EARTH.radius(realAnomalyEarth);
		double lEarth = EARTH.longitudeHelio(realAnomalyEarth); 

		//Ecliptic heliocentric latitude
		double repeatedSinTerm = sin(lonHelio - node);
		double latHelioEcl = asin(repeatedSinTerm * sinIncli);

		//Ecliptic heliocentric longitude
		double x = cos(lonHelio - node);
		double y = repeatedSinTerm * cosIncli;

		double lonHelioEcl = atan2(y,x) + node;

		//Projection of the radius over the ecliptic
		double rProj = r * cos(latHelioEcl);


		//Superior planets / Inferior planets
		double comSinTerm = sin(lonHelioEcl-lEarth);

		//First expression corresponds to the latitude of a superior planet while the second expression corresponds to inferior planets (avoids calculating both latitude but less readable)
		double delta = half >= EARTH.half ? lonHelioEcl + atan2(rEarth*comSinTerm, rProj-rEarth*cos(lonHelioEcl-lEarth)) 
		: Math.PI + lEarth + atan2(rProj*sin(lEarth-lonHelioEcl), rEarth-rProj*cos(lEarth-lonHelioEcl)); 
		delta = normalizePositive(delta);

		//Ecliptic latitude of the Planet
		double numBeta = rProj * tan(latHelioEcl) * sin(delta-lonHelioEcl);
		double denBeta = rEarth * comSinTerm;
		double beta = atan(numBeta / denBeta);

		//Angular Size
		double p = sqrt(rEarth*rEarth + r*r - 2*rEarth*r*cos(lonHelio - lEarth)*cos(latHelioEcl));

		double angular = arcAngularSize/p;

		//Phase
		double f = (1 + cos(delta-lonHelio))/2.0;

		//Magnitude
		double mag = magnitude + 5*log10(r*p/sqrt(f));


		//Conversion from ecliptic to equatorial Coordinates	
		EquatorialCoordinates equ = eclipticToEquatorialConversion.apply(EclipticCoordinates.of(delta, beta));

		return new Planet(name, equ, (float) angular, (float) mag);
	}

	/**
	 * Returns mean anomaly of the Planet
	 * 
	 * @param daysSinceJ2010
	 * 			Days since epoch J2010
	 * 			
	 * @return mean anomaly of the Planet at the given time
	 * 
	 */
	private double meanAnomaly(double daysSinceJ2010) {
		return termAnomaly * (daysSinceJ2010) + lonEpoch - peri;
	}

	/**
	 * Returns real anomaly of the Planet given its mean anomaly
	 * 
	 * @param meanAnomaly
	 * 			Mean anomaly of the Planet
	 * 
	 * @param e
	 * 			Orbital eccentricity
	 * 
	 * @return real anomaly of the Planet
	 */
	private double realAnomaly(double meanAnomaly) {
		return meanAnomaly + 2*exc*sin(meanAnomaly);
	}

	/**
	 * Returns the radius of the Planet (distance Planet-Sun) given the real anomaly of the Planet
	 * 
	 * @param realAnomaly
	 * 			Real anomaly of the Planet
	 * 
	 * @return radius of the Planet (distance Planet-Sun) at the given time
	 */
	private double radius(double realAnomaly) {
		return half * (1 - excSquared)/(1 + exc*cos(realAnomaly));
	}

	/**
	 * Returns heliocentric longitude
	 * 
	 * @param realAnomaly
	 * 			Real anomaly of the Planet
	 * 
	 * @return heliocentric longitude
	 */
	private double longitudeHelio(double realAnomaly) {
		return realAnomaly + peri;
	}
}
