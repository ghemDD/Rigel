package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ch.epfl.rigel.coordinates.EquatorialCoordinates.*;

import org.junit.jupiter.api.Test;

public class EquatorialCoordinatesTest {

	@Test 
	void ofFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			EquatorialCoordinates equ=EquatorialCoordinates.of(Angle.TAU, 0.25);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			EquatorialCoordinates equ=EquatorialCoordinates.of(0.25, Math.PI+1);
		});
	}
	
	@Test 
	void ofDegFailsWithUnvalidValues(){
		assertThrows(IllegalArgumentException.class, () -> {
			EquatorialCoordinates equ=EquatorialCoordinates.ofDeg(432, 767);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			EquatorialCoordinates equ=EquatorialCoordinates.ofDeg(890, 990);
		});
	}
	//add limit cases
	
	@Test 
	void gettersWellDefined() {
		EquatorialCoordinates equ1=ofDeg(29, 57);
		EquatorialCoordinates equ2=ofDeg(45, 87);
		
		assertEquals(Angle.ofDeg(29), equ1.ra());
		assertEquals(Angle.ofDeg(57), equ1.dec());
		assertEquals(Angle.toHr(Angle.ofDeg(29)), equ1.raHr());
		assertEquals(29, equ1.lonDeg(), 1e-5);
		assertEquals(57, equ1.latDeg(), 1e-5);
		
		assertEquals(Angle.ofDeg(45), equ2.ra());
		assertEquals(Angle.ofDeg(87), equ2.dec());
		assertEquals(Angle.toHr(Angle.ofDeg(45)), equ2.raHr());
		assertEquals(45, equ2.raDeg(), 1e-5);
		assertEquals(87, equ2.decDeg(), 1e-5);
	}
	
	@Test
	void toStringWorksOnKnownEquatorialCoordinates() {
	EquatorialCoordinates equ=EquatorialCoordinates.of(Angle.ofHr(1.5), Angle.ofDeg(45.00));
		assertEquals("(ra=1.5000h, dec=45.0000°)", equ.toString());
	}
	
	@Test
	void equalsThrowsUOE() {
		assertThrows(UnsupportedOperationException.class, () -> {
			var equ = ofDeg(67, 57);
			equ.equals(equ);
		});
	}

	@Test
	void hashCodeThrowsUOE() {
		assertThrows(UnsupportedOperationException.class, () -> {
			ofDeg(54, 45).hashCode();
		});
	}	
}
