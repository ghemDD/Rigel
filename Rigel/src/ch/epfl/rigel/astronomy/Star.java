package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import static ch.epfl.rigel.Preconditions.*;
/**
 * Represents a Star 
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class Star extends CelestialObject{
	private static final ClosedInterval COL_INT = ClosedInterval.of(-0.5, 5.5);
	private final int hipparcos;
	private final float color;
	private final double colorTemp;

	/**
	 * Constructor for Star
	 * 
	 * @param hipparcosId
	 * 			Hipparcos Id of the Star 
	 * 
	 * @param name 
	 * 			Name of the Star
	 * 
	 * @param equatorialPos 
	 * 			Equatorial coordinates of the Star
	 * 
	 * @param magnitude 
	 * 			Magnitude of the Star
	 * 
	 * @param colorIndex
	 * 			Color Index of the star
	 * 
	 * @throws IllegalArgumentException
	 * 			if hipparcosId is negative
	 * 			if colorIndex is not in the interval COL_INT
	 *
	 */
	public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
		super(name, equatorialPos, 0, magnitude);
		checkArgument(hipparcosId>=0);
		checkInInterval(COL_INT, colorIndex);
		hipparcos = hipparcosId;
		color = colorIndex;
		double repeated = 0.92 * color;
		colorTemp = 4600 * (1/(repeated + 1.7) + 1/(repeated + 0.62));
	}

	/**
	 * Getter for the hipparcos ID
	 * 
	 * @return hipparcos ID of the star
	 */
	public int hipparcosId() {return hipparcos;}

	/**
	 * Getter for the color temperature 
	 * 
	 * @return color temperature of the star 
	 */
	public int colorTemperature() {return (int) colorTemp;}
}
