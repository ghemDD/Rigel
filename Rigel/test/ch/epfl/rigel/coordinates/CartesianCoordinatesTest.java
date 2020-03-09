package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static ch.epfl.rigel.coordinates.CartesianCoordinates.*;

import ch.epfl.rigel.math.Polynomial;

public class CartesianCoordinatesTest {

	@Test
	void toStringWorksOnKnownCartesianCoordinates() {
		//Randomization
		CartesianCoordinates coor=of(2.0, 3.0);
		CartesianCoordinates coor1=of(7.784, 9.349);
		CartesianCoordinates coor2=of(5.563, 3.234);
		CartesianCoordinates coor3=of(2.999, 4.12);
		
		assertEquals("(x=2.0000, y=3.0000)", coor.toString());
		assertEquals("(x=7.7840, y=9.3490)", coor1.toString());
		assertEquals("(x=5.5630, y=3.2340)", coor2.toString());
		assertEquals("(x=2.9990, y=4.1200)", coor3.toString());
	}
	
	@Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            CartesianCoordinates.of(1.0, 3.0).equals("Test");
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
        	CartesianCoordinates.of(1.0, 3.0).hashCode();
        });
    }
}
