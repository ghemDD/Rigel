package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Represents a planet at a given location
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class Planet extends CelestialObject {

	/**
	 * Constructor for Planet
	 * 
	 * @param name 
	 * 			Name of the planet
	 * 
	 * @param equatorialPos 
	 * 			Equatorial Coordinates of the planet
	 * 
	 * @param angularSize 
	 * 			Angular Size of the planet
	 * 
	 * @param magnitude 
	 * 			Magnitude of the planet
	 * 
	 */
	public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
		super(name, equatorialPos, angularSize, magnitude);
	}
}
