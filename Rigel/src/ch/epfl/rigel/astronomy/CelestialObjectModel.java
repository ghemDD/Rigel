package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * Modeling of a celestial object
 * 
 * @param <O> Celestial object to be modeled
 * 
 * @author Nael Ouerghemi (310435)
 */
public interface CelestialObjectModel<O> {

	/**
	 * Modeling of a celestial object given number of days since J2010 and an eclipticToEquatorialConversion
	 * 
	 * @param daysSinceJ2010
	 * 			number of days since Epoch J2010
	 * 
	 * @param eclipticToEquatorialConversion
	 * 			Conversion from ecliptic to equatorial coordinates used
	 * 
	 * @return Generic Celestial Object to be modeled
	 */
	public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
