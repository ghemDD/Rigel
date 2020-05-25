package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import static ch.epfl.rigel.Preconditions.*;

import java.util.Locale;

/**
 * Represents the Moon at a given instant
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class Moon extends CelestialObject{

	private final float phase;
	private static final ClosedInterval PHASE_INT = ClosedInterval.of(0, 1);

	/**
	 * Constructor for the moon
	 * 
	 * @param equatorialPos 
	 * 			Equatorial Coordinates of the position of the moon
	 * 
	 * @param angularSize 
	 * 			Angular Size of the moon
	 * 
	 * @param magnitude 
	 * 			Magnitude of the moon
	 * 
	 * @param phase 
	 * 			Phase of the moon
	 */
	public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
		super("Lune", equatorialPos, angularSize, magnitude);
		this.phase = (float) checkInInterval(PHASE_INT, phase);
	}

	/**
	 * @see CelestialObject#info()
	 */
	@Override
	public String info() {
		return String.format(Locale.ROOT, name()+" (%.1f%%)", phase*100);
	}
}
