package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import static ch.epfl.rigel.math.Angle.*;
import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;

/**
 * 
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
		// TODO Auto-generated method stub

		double mean=TAU/(365.242191)*daysSinceJ2010+eps-peri;
		double real=mean + 2* exc * Math.sin(mean);

		double delta= real + peri;
		double angularSize=angular*(1+exc*Math.cos(real))/(1-exc*exc);

		return new Sun(of(mean, 0), eclipticToEquatorialConversion.apply(of(delta, 0)), (float) angularSize, (float) mean);
	}

}
