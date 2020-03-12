package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import static ch.epfl.rigel.coordinates.CartesianCoordinates.*;

import ch.epfl.rigel.math.Polynomial;
import ch.epfl.test.TestRandomizer;

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
    void ofWorksWithAllValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            var x = rng.nextDouble(-1000000, 1000000);
            var y = rng.nextDouble(-1000000, 1000000);
            var c = CartesianCoordinates.of(x, y);
            assertEquals(x, c.x());
            assertEquals(y, c.y());
        }
    }

    @Test
    void toStringWorksProperly(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            var x = rng.nextDouble(-1000000, 1000000);
            var y = rng.nextDouble(-1000000, 1000000);
            var c = CartesianCoordinates.of(x, y);
            var s = String.format(Locale.ROOT, "(x=%.4f, y=%.4f)", x, y);
            assertEquals(s, c.toString());
        }
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
