package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Represents a planet at a given location
 * @author Nael Ouerghemi
 *
 */
public final class Planet extends CelestialObject {

	public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
		super(name, equatorialPos, angularSize, magnitude);
	}

}
