package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import static ch.epfl.rigel.Preconditions.*;
/**
 * Represents a Star 
 * @author Nael Ouerghemi
 *
 */
public final class Star extends CelestialObject{
	private final ClosedInterval colint=ClosedInterval.of(-0.5, 5.5);
	private int hipparcos;
	private float color;
	
	//Angular size of the star ?
	Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
		super(name, equatorialPos, 0, magnitude);
		checkArgument(hipparcosId>=0);
		checkInInterval(colint, colorIndex);
		hipparcos=hipparcosId;
		color=colorIndex;
	}
	
	/**
	 * Getter for the hipparcos ID
	 * @return hipparcos ID of the star
	 */
	public int hipparcosId() {return hipparcos;}
	
	/**
	 * Getter for the color temperature 
	 * @return color temperature of the star 
	 */
	public int colorTemperature() {return (int) color;}
}
