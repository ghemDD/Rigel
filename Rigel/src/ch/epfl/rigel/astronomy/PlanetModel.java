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

	private PlanetModel(String name, double revPeriod, double lonEpoch, double peri, double exc, double half, double incli, double node, double angularSize, double magnitude) {
		this.name = name;
		this.revPeriod = revPeriod;
		this.lonEpoch = ofDeg(lonEpoch);
		this.peri = ofDeg(peri);
		this.exc = exc;
		this.half = half;
		this.incli = ofDeg(incli);
		this.node = ofDeg(node);
		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}

	/**
	 * Immutable list of all planets used to compute their positions
	 */
	public static List<PlanetModel> ALL=List.copyOf(Arrays.asList(PlanetModel.values()));
	private String name;
	private double revPeriod;
	private double lonEpoch;
	private double peri;
	private double exc;
	private double half;
	private double incli;
	private double node;
	private double angularSize;
	private double magnitude;

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

		//Mean anomaly
		double meanAnomaly = meanAnomaly(daysSinceJ2010, revPeriod, lonEpoch, peri);

		//Real anomaly
		double realAnomaly = realAnomaly(meanAnomaly, exc);

		//Radius (distance from this planet to the sun)
		double r = radius(realAnomaly, half, exc);

		//Heliocentric longitude
		double lonHelio = realAnomaly + peri;

		//Previous variables adapted for the Earth : meanAnomaly, realAnomaly, radius, heliocentric longitude
		double meanAnomalyEarth = meanAnomaly(daysSinceJ2010, EARTH.revPeriod, EARTH.lonEpoch, EARTH.peri);
		double realAnomalyEarth = realAnomaly(meanAnomalyEarth, EARTH.exc);
		double rEarth = radius(realAnomalyEarth, EARTH.half, EARTH.exc);
		double lEarth = realAnomalyEarth + EARTH.peri; 

		//Ecliptic heliocentric latitude
		double repeatedSinTerm = sin(lonHelio - node);
		double latHelioEcl = asin(repeatedSinTerm * sin(incli));

		//Ecliptic heliocentric longitude
		double x = cos(lonHelio - node);
		double y = repeatedSinTerm * cos(incli);

		double lonHelioEcl = atan2(y,x) + node;

		//Projection of the radius over the ecliptic
		double rProj = r * cos(latHelioEcl);


		//Superior planets / Inferior planets
		double comSinTerm = sin(lonHelioEcl-lEarth);
		double deltaSup = lonHelioEcl + atan2(rEarth*comSinTerm, rProj-rEarth*cos(lonHelioEcl-lEarth));
		double deltaInf = Math.PI + lEarth + atan2(rProj*sin(lEarth-lonHelioEcl), rEarth-rProj*cos(lEarth-lonHelioEcl));

		double delta = revPeriod >= EARTH.revPeriod ? deltaSup : deltaInf; 
		delta = normalizePositive(delta);

		//Ecliptic latitude of the Planet
		double numBeta = rProj * tan(latHelioEcl) * sin(delta-lonHelioEcl);
		double denBeta = rEarth * comSinTerm;
		double beta = atan(numBeta / denBeta);

		//Angular Size
		double p = sqrt(rEarth*rEarth + r*r - 2*rEarth*r*cos(lonHelio - lEarth)*cos(latHelioEcl));

		double angular = ofArcsec(angularSize)/p;

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
	 * @param period
	 * 			Period of revolution (in tropic years)
	 * 
	 * @param lonJ2010
	 * 			Longitude of the Planet at J2010
	 * 
	 * @param perigee
	 * 			Longitude of the perigee
	 * 			
	 * @return mean anomaly of the Planet at the given time
	 * 
	 */
	private double meanAnomaly(double daysSinceJ2010, double period, double lonJ2010, double perigee) {
		return (TAU/365.242191) * (daysSinceJ2010/period) + lonJ2010 - perigee;
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
	private double realAnomaly(double meanAnomaly, double e) {
		return meanAnomaly + 2*e*sin(meanAnomaly);
	}

	/**
	 * Return the radius of the Planet (distance Planet-Sun) given the real anomaly of the Planet
	 * 
	 * @param realAnomaly
	 * 			Real anomaly of the Planet
	 * 
	 * @param h
	 * 			Semi major axis of the orbit
	 * 
	 * @param e
	 * 			Orbital eccentricity
	 * 
	 * @return radius of the Planet (distance Planet-Sun) at the given time
	 */
	private double radius(double realAnomaly, double h, double e) {
		return h * (1 - e*e)/(1 + e*cos(realAnomaly));
	}
}
