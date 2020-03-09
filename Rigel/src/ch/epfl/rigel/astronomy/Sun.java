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
		super("Soleil", equatorialPos, angularSize,(float) -26.7);
		requireNonNull(eclipticPos);
		this.meanAnomaly=meanAnomaly;
		this.eclipticPos=eclipticPos;
	}
	
	public EclipticCoordinates eclipticPos() {return eclipticPos;}
	
	public double meanAnomaly() {return meanAnomaly;}

}
