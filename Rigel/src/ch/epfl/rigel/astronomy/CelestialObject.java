package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;



import static ch.epfl.rigel.Preconditions.*;
import static java.util.Objects.requireNonNull;

/**
 * Represents celestial objects
 * 
 * @author Nael Ouerghemi (310435)
 */
public abstract class CelestialObject {

	private final String name;
	private final EquatorialCoordinates equatorialPos;
	private final float angularSize;
	private final float magnitude;


	/**
	 * Constructor for celestial objects
	 * 
	 * @param name 
	 * 			name of the celestial object
	 * 
	 * @param equatorialPos 
	 * 			Equatorial Coordinates
	 * 
	 * @param angularSize
	 * 			Angular size
	 * 
	 * @param magnitude
	 * 			Magnitude
	 * 
	 * @throws IllegalArgumentException
	 * 			if angularSize is negative
	 * 
	 * @throws NullPointerException
	 * 			if name/equatorialPos is null
	 */
	CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
		checkArgument(angularSize>=0);
		this.name = requireNonNull(name);
		this.equatorialPos = requireNonNull(equatorialPos);

		this.angularSize = angularSize;
		this.magnitude = magnitude;
	}

	/**
	 * Getter for the name attribute
	 * 
	 * @return name
	 */
	public String name() {return name;}

	/**
	 * Getter for angular size
	 * 
	 * @return angular size
	 */
	public double angularSize() {return angularSize;}

	/**
	 * Getter for magnitude
	 * 
	 * @return magnitude
	 */
	public double magnitude() {return magnitude;}

	/**
	 * Getter for equatorial position
	 * 
	 * @return Equatorial Position
	 */
	public EquatorialCoordinates equatorialPos() {return equatorialPos;}

	/**
	 * Returns informations about the celestial object
	 * 
	 * @return String containing the informations about the celestial object
	 */
	public String info() {return name();}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {return info();}
}
