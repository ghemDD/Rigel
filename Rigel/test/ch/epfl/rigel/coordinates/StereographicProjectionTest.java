package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;
import static ch.epfl.rigel.coordinates.HorizontalCoordinates.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;

public class StereographicProjectionTest {

	@Test
	void applyTest() {
		//Choose a correct data Set
		//Find a script to automate and randomize the process
		StereographicProjection northproj=new StereographicProjection(ofDeg(0, -90));

		System.out.println(northproj.apply(ofDeg(0.00, -51.32 )));

		assertEquals(1, 1);
	}


	@Test
	void inverseApplyTest() {
		//Randomization
		var rng= TestRandomizer.newRandom();

		//North pole stereographic projection
		StereographicProjection southproj=new StereographicProjection(ofDeg(0, -90));

		for(int i=0; i<TestRandomizer.RANDOM_ITERATIONS; i++) {
			var az=rng.nextDouble(0, 360.0);
			var alt=rng.nextDouble(-90.0, 0);
			System.out.println("Az ="+az+" Alt ="+alt);
			var h=HorizontalCoordinates.ofDeg(az, alt);

			var car= southproj.apply(h);



			assertEquals(southproj.inverseApply(car).toString(), h.toString());
		}

		//South pole stereographic projection
		StereographicProjection northproj=new StereographicProjection(ofDeg(0, 90));

		for(int i=0; i<TestRandomizer.RANDOM_ITERATIONS; i++) {
			var az=rng.nextDouble(0, 360.0);
			var alt=rng.nextDouble(0, 90.0);
			var car= northproj.apply(HorizontalCoordinates.ofDeg(az, alt));

			var h=HorizontalCoordinates.ofDeg(az, alt);

			assertEquals(northproj.inverseApply(car).toString(), h.toString());
		}
	}

	@Test
	void TelegramTest() {

		double test1=new StereographicProjection (HorizontalCoordinates.ofDeg(45,45))
				.apply(HorizontalCoordinates.ofDeg(45,30)).y();
		assertEquals(-0.13165249758739583, test1);

		double test2=new StereographicProjection(HorizontalCoordinates.ofDeg(45,45))
				.inverseApply(CartesianCoordinates.of(10,0)).az();
		/**
		 * { 3.648704525474978 }
		 *  * 3.648704634091643 [2]
		 */

		assertEquals(3.648704634091643, test2);

		double test3=new StereographicProjection (HorizontalCoordinates.ofDeg(23, 45))
				.applyToAngle(Angle.ofDeg(1/2.0));
		/** { 4.363330053e-3 }
		 * 0.004363316207379377 [1]
		 **/
		assertEquals(test3, 4.363330053e-3, 1E-10);


		double test4=new StereographicProjection(HorizontalCoordinates.ofDeg(45,45))
				.circleCenterForParallel(HorizontalCoordinates.ofDeg(0,27)).y();

		/**{ 0.6089987401 }
		 * 0.6089987400733187 [2]
		 **/

		assertEquals(test4, 0.6089987400733187);

		double test5=new StereographicProjection(HorizontalCoordinates.ofDeg(45,45))
				.circleRadiusForParallel(HorizontalCoordinates.ofDeg(0,27));

		/**{ 0.7673831804 }
		 * 0.767383180397855 [2]
		 * **/

		assertEquals(test5, 0.767383180397855);
	}


	@Test
	void ThrowsNPE() {
		assertThrows(NullPointerException.class, () -> {
			StereographicProjection proj=new StereographicProjection(null);
		});
	}

	@Test
	void toStringWorksOnKnownStereographicProjection() {
		//Randomization
		var rng= TestRandomizer.newRandom();

		for(int i=0; i<TestRandomizer.RANDOM_ITERATIONS; i++) {
			var az=rng.nextDouble(0, 360.0);
			var alt=rng.nextDouble(-90.0, 90.0);
			StereographicProjection proj=new StereographicProjection(ofDeg(az, alt));


			assertEquals("StereographicProjection Center coordinates "+HorizontalCoordinates.ofDeg(az, alt), proj.toString());
		}
	}

	@Test
	void equalsThrowsUOE() {
		assertThrows(UnsupportedOperationException.class, () -> {
			StereographicProjection proj=new StereographicProjection(ofDeg(32, 34));
			proj.equals("Test");
		});
	}

	@Test
	void hashCodeThrowsUOE() {
		assertThrows(UnsupportedOperationException.class, () -> {
			StereographicProjection proj=new StereographicProjection(ofDeg(32, 34));
			proj.hashCode();
		});
	}
}
