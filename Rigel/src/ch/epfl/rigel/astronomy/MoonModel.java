package ch.epfl.rigel.astronomy;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static ch.epfl.rigel.math.Angle.normalizePositive;
import static ch.epfl.rigel.math.Angle.ofDeg;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Modeling of Moon 
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum MoonModel implements CelestialObjectModel<Moon>{

	MOON;

	private static final double MEAN_LON = ofDeg(91.929336);
	private static final double MEAN_PERI = ofDeg(130.143076);
	private static final double NODE_LON = ofDeg(291.682547);
	private static final double I = ofDeg(5.145396);
	private static final double E = 0.0549;
	private static final double MOON_FROM_EARTH = ofDeg(0.5181);
	private static final double COS_INCLI = cos(I);
	private static final double SIN_INCLI = sin(I); 

	/**
	 * Returns a representation of the moon at a given time depending on the epoch J2010 and location
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
	public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

		//Sun longitude and mean anomaly 
		Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
		double lonSun = sun.eclipticPos().lon();
		double sinMeanSun = sin(sun.meanAnomaly());

		//Mean orbital longitude Moon
		double lon = ofDeg(13.1763966)*daysSinceJ2010 + MEAN_LON;

		//Mean anomaly Moon
		double meanAnomaly = lon - ofDeg(0.1114041)*daysSinceJ2010 - MEAN_PERI;


		//Correction of mean anomaly of Moon (Sun influence)
		double ev = ofDeg(1.2739) * sin(2*(lon-lonSun) - meanAnomaly);
		double ae = ofDeg(0.1858) * sinMeanSun;
		double athree = ofDeg(0.37) * sinMeanSun;

		double meancor = meanAnomaly + ev - ae - athree;

		//Correction of ecliptic longitude of Moon	
		double ec = ofDeg(6.2886) * sin(meancor);
		double aFour = ofDeg(0.214) * sin(2 * meancor);
		double lonCor = lon + ev + ec - ae + aFour;
		double v = ofDeg(0.6583) * sin(2 * (lonCor - lonSun));

		double lonReal = lonCor + v;

		//Node Position
		double nodeMean = NODE_LON - ofDeg(0.0529539) * daysSinceJ2010;
		double nodeCor = nodeMean - ofDeg(0.16) * sinMeanSun; 


		//Ecliptic longitude of Moon
		double comSinTerm = sin(lonReal - nodeCor);
		double ylon = comSinTerm * COS_INCLI;
		double xlon = cos(lonReal - nodeCor);
		double lonMoon = normalizePositive(atan2(ylon , xlon) + nodeCor);

		//Ecliptic latitude of Moon
		double termlat = comSinTerm * SIN_INCLI;
		double latMoon = asin(termlat);


		//Moon phase
		double cosTerm = cos(lonReal - lonSun);
		double f = (1 - cosTerm)/2;

		//Angular Size
		double num = 1 - E*E;
		double den = 1 + E * cos(meancor + ec);
		double p = num / den;

		double angularSize = MOON_FROM_EARTH / p;

		//Conversion from ecliptic to equatorial coordinates
		EquatorialCoordinates equ = eclipticToEquatorialConversion.apply(of(lonMoon, latMoon));	

		return new Moon(equ, (float) angularSize, 0f,(float) f);
	}
}
