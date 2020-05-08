package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;


import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import static java.util.Objects.requireNonNull;

/**
 * Represents the Sun at a given instant
 * @author Nael Ouerghemi (310435)
 *
 */
public final class Sun extends CelestialObject{

	private EclipticCoordinates eclipticPos;
	private float meanAnomaly;

	/**
	 * Constructor of the sun
	 * 
	 * @param eclipticPos 
	 * 			Ecliptic Coordinates of the sun
	 * 
	 * @param equatorialPos 
	 * 			Equatorial Coordinates of the sun
	 * 
	 * @param angularSize
	 * 			Angular size of the sun
	 * 
	 * @param meanAnomaly
	 * 			Mean anomaly of the sun
	 * 
	 * @throws NullPointerException
	 * 			if eclipticPos is null
	 */
	public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
		super("Soleil", equatorialPos, angularSize, -26.7f);
		requireNonNull(eclipticPos);
		this.meanAnomaly = meanAnomaly;
		this.eclipticPos = eclipticPos;
	}

	/**
	 * Getter for the ecliptic coordinates
	 * 
	 * @return Ecliptic coordinates ecliptic coordinates of the sun
	 */
	public EclipticCoordinates eclipticPos() {return eclipticPos;}

	/**
	 * Getter for the mean anomaly
	 * 
	 * @return mean anomaly of the sun
	 */
	public double meanAnomaly() {return meanAnomaly;}
}
