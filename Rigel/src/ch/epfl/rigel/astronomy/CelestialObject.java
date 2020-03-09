package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import static ch.epfl.rigel.Preconditions.*;
import static java.util.Objects.requireNonNull;

/**
 * Represents celestial objects
 * @author Nael Ouerghemi
 *
 */
public abstract class CelestialObject {

	private String name;
	private EquatorialCoordinates equatorialPos;
	private float angularSize;
	private float magnitude;
	
	
	CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
		checkArgument(angularSize>=0);
		requireNonNull(name);
		requireNonNull(equatorialPos);
		
		this.name=name;
		this.equatorialPos=equatorialPos;
		this.angularSize=angularSize;
		this.magnitude=magnitude;
	}
	
	public String name() {return name;}
	
	public double angularSize() {return angularSize;}
	
	public double magnitude() {return magnitude;}
	
	public EquatorialCoordinates equatorialPos() {return equatorialPos;}
	
	public String info() {return name();}
	
	@Override
	public String toString() {return name();}
}
