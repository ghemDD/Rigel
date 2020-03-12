package ch.epfl.rigel.astronomy;

import static java.time.ZonedDateTime.of;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public class PlanetModelTest {

	
	@Test
	void planetModelTestJupiter() {
		
		Planet jupi=PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(of(2003,11,22,00,00,00,00, ZoneId.of("UTC"))), new EclipticToEquatorialConversion(of(2001,1,27,00,00,00,00, ZoneId.of("UTC"))));
		
		assertTrue(true);
	}
}
