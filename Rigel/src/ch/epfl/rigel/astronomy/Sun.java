package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import static java.util.Objects.requireNonNull;

/**
 * Represents the Sun at a given instant
 * @author Nael Ouerghemi
 *
 */
public final class Sun extends CelestialObject{
	
	private EclipticCoordinates eclipticPos;
	private float meanAnomaly;
	
	public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
		super("Soleil", equatorialPos, angularSize, -26.7f);
		requireNonNull(eclipticPos);
		this.meanAnomaly=meanAnomaly;
		this.eclipticPos=eclipticPos;
	}
	
	/**
	 * Getter for the ecliptic coordinates
	 * @return Ecliptic coordinates of the sun
	 */
	//Immuabilité ?
	public EclipticCoordinates eclipticPos() {return eclipticPos;}
	
	/**
	 * Getter for the mean anomaly
	 * @return mean anomaly of the sun
	 */
	public double meanAnomaly() {return meanAnomaly;}

}
