package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import static ch.epfl.rigel.math.Angle.*;

import java.util.List;

/**
 * 
 * @author Nael Ouerghemi
 *
 */
public enum PlanetModel implements CelestialObjectModel<Planet>{

	MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
			0.387098, 7.0051, 48.449, 6.74f, -0.42f),
	VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
			0.723329, 3.3947, 76.769, 16.92f, -4.40f),
	EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
			0.999985, 0, 0, 0, 0),
	MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
			1.523689, 1.8497, 49.632, 9.36f, -1.52f),
	JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
			5.20278, 1.3035, 100.595, 196.74f, -9.40f),
	SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
			9.51134, 2.4873, 113.752, 165.60f, -8.88f),
	URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
			19.21814, 0.773059, 73.926961, 65.80f, -7.19f),
	NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
			30.1985, 1.7673, 131.879, 62.20f, -6.87f);

	private PlanetModel(String name, double tropic, double eps, double peri, double exc, double half, double incli, double node, float angularSize, float magnitude) {
		this.name=name;
		this.tropic=tropic;
		this.eps=eps;
		this.peri=peri;
		this.exc=exc;
		this.half=half;
		this.incli=incli;
		this.node=node;
		this.angularSize=angularSize;
		this.magnitude=magnitude;
	}

	public static List<PlanetModel> ALL;
	private String name;
	private double tropic;
	private double eps;
	private double peri;
	private double exc;
	private double half;
	private double incli;
	private double node;
	private float angularSize;
	private float magnitude;

	@Override
	public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
		
		double mean=TAU/(365.242191)*daysSinceJ2010/(tropic)+ofDeg(eps)-ofDeg(peri);
		double real=mean+2*exc*Math.sin(mean);

		double r=half*(1-exc*exc)/(1+exc*Math.cos(real));
		double l=real+ofDeg(peri);
		
		double meanEarth=TAU/(365.242191)*daysSinceJ2010/(EARTH.tropic)+ofDeg(EARTH.eps)-ofDeg(EARTH.peri);
		double realEarth=meanEarth+2*exc*Math.sin(meanEarth);
		double rEarth=EARTH.half*(1-EARTH.exc*EARTH.exc)/(1+EARTH.exc*Math.cos(realEarth));
		double lEarth=realEarth+ofDeg(EARTH.peri);
		
		double pos=Math.asin(Math.sin(l-ofDeg(node))*Math.sin(ofDeg(incli)));

		double rp=r*Math.cos(pos);
		double lp=Math.atan2(Math.asin(l-ofDeg(node))*Math.cos(ofDeg(incli)), Math.cos(l-ofDeg(node)))+ofDeg(node);

		double delta;

		//Superior planets
		if (magnitude<-4.40f || magnitude==-1.52f) {
			delta=lp+Math.atan2(rEarth*Math.sin(lp-lEarth), rp-rEarth*Math.cos(lp-lEarth));
		}

		//Inner planets
		else {
			delta=Math.PI+lEarth+Math.atan2(rp*Math.sin(lEarth-lp), rEarth-rp*Math.cos(lEarth-lp));
		}

		double beta=Math.atan2(rp*Math.tan(pos)*Math.sin(delta-lp), rEarth*Math.sin(lp-lEarth));

		double p=Math.sqrt(rEarth*rEarth + r*r - 2*rEarth*r*Math.cos(l-lEarth)*Math.cos(pos));
		float angular=(float) (angularSize/p);

		double f=(1+Math.cos(delta-l))/2.0;
		float mag=(float) (magnitude + 5*Math.log10(r*p/Math.sqrt(f)));

		return new Planet(name, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(delta, beta)), angular, mag);
	}
}
