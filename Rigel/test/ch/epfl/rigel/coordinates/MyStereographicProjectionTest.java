package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;
import static ch.epfl.rigel.coordinates.HorizontalCoordinates.*;

import org.junit.jupiter.api.Test;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;

public class MyStereographicProjectionTest {

	@Test
	void applyTest() {
		//Choose a correct data Set
		//Find a script to automate and randomize the process
		StereographicProjection northproj=new StereographicProjection(ofDeg(0, -90));

		//System.out.println(northproj.apply(ofDeg(0.00, -51.32)));
		//(x=0.0000, y=0.3510)
		//System.out.println(northproj.apply(ofDeg(83.8, -45.45)));
		//(x=0.4072, y=0.0442)

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
			//System.out.println("Az ="+az+" Alt ="+alt);
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
	void circleCenterForParallelWorksForValidValues() {
		var rng = TestRandomizer.newRandom();
		for(int i=0; i<100; ++i) {
			var azCenter = rng.nextDouble(0, 360);
			var altCenter = rng.nextDouble(-90, 90);
			var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
			var s = new StereographicProjection(h_center);

			for (int j = 0; j < 1000; ++j) {
				var azDeg = rng.nextDouble(0, 360);
				var altDeg = rng.nextDouble(-90, 90);
				var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);
				double yCenter = Math.cos(h_center.lat()) / (Math.sin(h1.lat()) + Math.sin(h_center.lat()));
				assertEquals(0.0, s.circleCenterForParallel(h1).x());
				assertEquals(yCenter, s.circleCenterForParallel(h1).y());
			}
		}
	}

	@Test
	void circleCenterForParallelOutputsInfinity(){
		var h_center = HorizontalCoordinates.ofDeg(0, 0);
		var h = HorizontalCoordinates.ofDeg(0, 0);
		var s = new StereographicProjection(h_center);
		double y = Math.cos(h_center.lat()) / (Math.sin(h.lat()) + Math.sin(h_center.lat()));
		assertEquals(y, s.circleCenterForParallel(h).y());
	}

	@Test
	void circleRadiusForParallelWorksForValidValues() {
		var rng = TestRandomizer.newRandom();
		for(int i=0; i<100; ++i) {
			var azCenter = rng.nextDouble(0, 360);
			var altCenter = rng.nextDouble(-90, 90);
			var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
			var s = new StereographicProjection(h_center);

			for (int j = 0; j < 1000; ++j) {
				var azDeg = rng.nextDouble(0, 360);
				var altDeg = rng.nextDouble(-90, 90);
				var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);
				double radius = Math.cos(h1.lat()) / (Math.sin(h1.lat()) + Math.sin(h_center.lat()));
				assertEquals(radius, s.circleRadiusForParallel(h1));
			}
		}
	}

	@Test
	void circleRadiusForParallelOutputsInfinity(){
		var h_center = HorizontalCoordinates.ofDeg(0, 0);
		var h = HorizontalCoordinates.ofDeg(0, 0);
		var s = new StereographicProjection(h_center);
		double radius = Math.cos(h.lat()) / (Math.sin(h.lat()) + Math.sin(h_center.lat()));
		assertEquals(radius, s.circleCenterForParallel(h).y());
	}

	@Test
	void applyToAngleWorksWithAllValues(){
		var s = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 50));

		for(int i=0; i<1000; ++i) {
			var angle = Math.random() * Angle.TAU;
			double equation = 2 * Math.tan(angle / 4);
			assertEquals(equation, s.applyToAngle(angle));
		}
	}

	@Test
	void applyWorksWithAllValues(){
		var rng = TestRandomizer.newRandom();
		for(int i=0; i<100; ++i) {
			var azCenter = rng.nextDouble(0, 360);
			var altCenter = rng.nextDouble(-90, 90);
			var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
			var s = new StereographicProjection(h_center);

			for (int j = 0; j < 1000; ++j) {
				var azDeg = rng.nextDouble(0, 360);
				var altDeg = rng.nextDouble(-90, 90);

				var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);

				double lambdaDelta = h1.lon() - h_center.lon();
				double d = 1 / (1 + Math.sin(h1.lat()) * Math.sin(h_center.lat()) + Math.cos(h1.lat()) * Math.cos(h_center.lat()) * Math.cos(lambdaDelta));

				double x = d * Math.cos(h1.lat()) * Math.sin(lambdaDelta);
				double y = d * (Math.sin(h1.lat()) * Math.cos(h_center.lat()) - Math.cos(h1.lat()) * Math.sin(h_center.lat()) * Math.cos(lambdaDelta));

				assertEquals(x, s.apply(h1).x());
				assertEquals(y, s.apply(h1).y());
			}
		}
	}

	@Test
	void inverseApplyWorksWithAllValues(){
		var rng = TestRandomizer.newRandom();
		for(int i=0; i<100; ++i) {
			var azDeg = rng.nextDouble(0, 360);
			var altDeg = rng.nextDouble(-90, 90);
			var h_center = HorizontalCoordinates.ofDeg(azDeg, altDeg);
			var s = new StereographicProjection(h_center);

			for (int j = 0; j < 1000; ++j) {
				var x = rng.nextDouble(-1000, 1000);
				var y = rng.nextDouble(-1000, 1000);

				var c = CartesianCoordinates.of(x, y);

				double rho = Math.sqrt(c.x() * c.x() + c.y() * c.y());

				double sinc = (2 * rho) / (rho * rho + 1);
				double cosc = (1 - rho * rho) / (rho * rho + 1);

				double lambda = Angle.normalizePositive(Math.atan2(c.x() * sinc, rho * Math.cos(h_center.lat()) * cosc - c.y() * Math.sin(h_center.lat()) * sinc) + h_center.lon());
				double fi = Math.asin(cosc * Math.sin(h_center.lat()) + ((c.y() * sinc * Math.cos(h_center.lat())) / rho));

				//System.out.println(lambda + " " + fi);

				var result = s.inverseApply(c);
				assertEquals(lambda, result.lon());
				assertEquals(fi, result.lat());
			}
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
		
		double test6 =  new StereographicProjection(HorizontalCoordinates.ofDeg(45,20))
			    .inverseApply(CartesianCoordinates.of(0,25)).az();
		
		assertEquals(3.9269908169872414, test6);
		
		double test7 = new StereographicProjection(HorizontalCoordinates.ofDeg(45,20))
				.inverseApply(CartesianCoordinates.of(0,25)).alt();
		
		assertEquals(-0.2691084761522857, test7);
						
		double test3=new StereographicProjection (HorizontalCoordinates.ofDeg(23, 45))
				.applyToAngle(Angle.ofDeg(1/2.0));
		/** { 4.363330053e-3 }
		 * 0.004363316207379377 [1]
		 **/
		assertEquals(test3, 0.00436333005262522, 1E-10);


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
