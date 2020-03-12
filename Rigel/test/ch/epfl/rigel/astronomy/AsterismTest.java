package ch.epfl.rigel.astronomy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class AsterismTest {

	@Test
	void AsterismFailsWithInvalidInput() {
		assertThrows(IllegalArgumentException.class, () -> {
			List<Star> stars=new ArrayList<Star>();
			Asterism a = new Asterism(stars);
		});
	}

	@Test
	void starsReturnDefensiveCopyOf() {
		List<Star> stars=new ArrayList<Star>();
		List<Star> stars1=new ArrayList<Star>();
		stars.add(new Star(78, "Rigel", EquatorialCoordinates.ofDeg(34, 34), -7, (float) 2.5));
		
		Asterism a = new Asterism(stars);
		stars1=a.stars();
		
		//copyOf returns immutable copy of the list
		//stars1.add(new Star(89, "Patrick", EquatorialCoordinates.ofDeg(34, 34), -8, (float) 3.5));
		
		//assertFalse(a.stars().equals(stars1));
		assertTrue(a.stars().equals(stars));
		assertFalse(a.stars()==stars);
	}

}
