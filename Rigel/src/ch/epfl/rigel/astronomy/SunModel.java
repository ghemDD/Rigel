package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import static ch.epfl.rigel.math.Angle.*;
import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;

/**
 * Modelisation of the position of the sun
 * @author Nael Ouerghemi
 *
 */
public enum SunModel implements CelestialObjectModel<Sun>{

	SUN();

	private SunModel() {

	}

	private double eps=ofDeg(279.557208);
	private double peri=ofDeg(283.112438);
	private double exc=0.016705;
	private double angular=ofDeg(0.533128);

	@Override
	public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

		double mean=(TAU/(365.242191))*daysSinceJ2010+eps-peri;
		double real=mean + 2 * exc * Math.sin(mean);

		double delta= normalizePositive(real + peri);
		
		double angularSize=angular*(1+exc*Math.cos(real))/(1-exc*exc);
		EquatorialCoordinates equ=eclipticToEquatorialConversion.apply(of(delta, 0));
		
		//Printers
		System.out.println("Days = "+daysSinceJ2010);
		System.out.println("Mean anomaly = "+toDeg(mean));
		System.out.println("Real anomaly = "+toDeg(real));
		System.out.println("Delta = "+toDeg(delta));
		System.out.println("Angular Size = "+toDeg(angularSize));
		System.out.println("Declination = "+equ.decDeg());

		return new Sun(of(mean, 0), equ, (float) angularSize, (float) mean);
	}

}
