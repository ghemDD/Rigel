package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import static ch.epfl.rigel.Preconditions.*;

import java.util.Locale;

/**
 * Represents the Moon at a given instant
 * @author Nael Ouerghemi
 *
 */
public final class Moon extends CelestialObject{

	private float phase;
	public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
		super("Lune", equatorialPos, angularSize, magnitude);
		ClosedInterval phaseInt=ClosedInterval.of(0, 1);
		checkInInterval(phaseInt, phase);
		this.phase=phase;
	}
	
	@Override
	public String info() {
		return String.format(Locale.ROOT, name()+" (%.1f%%)", phase*100);
	}

}
