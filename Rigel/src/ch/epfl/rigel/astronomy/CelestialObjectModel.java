package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
/**
 * Modelisation of a celestial object
 * @author Nael Ouerghemi
 *
 * @param <O> Celestial object to be modelised
 */
public interface CelestialObjectModel<O> {

	/**
	 * 
	 * @param daysSinceJ2010
	 * @param eclipticToEquatorialConversion
	 * @return
	 */
	public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
