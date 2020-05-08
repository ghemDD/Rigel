package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.astronomy.Sun;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import static ch.epfl.rigel.math.Angle.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyCanvasPainter {

	private Canvas canvas;
	private GraphicsContext graphicsContext;
	private static final ClosedInterval MAGNITUDE_INT = ClosedInterval.of(-2, 5);
	private Map<Star, CartesianCoordinates> starTransformed;
	private Map<Star, Double> starRadius;
	private double[] starPoints;

	/**
	 * Constructor for skyCanvasPainter
	 * 
	 * @param canvas
	 * 			Canvas on which are printed the celestial object
	 */
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas = canvas;
		graphicsContext = this.canvas.getGraphicsContext2D();
		starTransformed = new HashMap<Star, CartesianCoordinates>();
		starRadius = new HashMap<Star, Double>();
	}

	public void clear() {
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	/**
	 * Draws the entire sky in the following order : asterisms, stars, horizon, planets, sun, moon
	 * 
	 * @param sky
	 * 			Observed sky used
	 * 
	 * @param projection
	 * 			Stereographic projection used
	 * 
	 * @param transform
	 * 			Transform used
	 */
	public void drawSky(ObservedSky sky, StereographicProjection projection, Transform transform) {	
		clear();
		drawAsterisms(sky, projection, transform);
		drawStars(sky, projection, transform);
		//testPath(transform);
		drawHorizon(sky, projection, transform);
		drawPlanets(sky, projection, transform);
		drawSun(sky, projection, transform);
		drawMoon(sky, projection, transform);
	}

	/**
	 * Draw all the Planets
	 * 
	 * @param sky
	 * 			Observed Sky
	 * 
	 * @param projection
	 * 			Stereographic Projection used
	 * 
	 * @param transform
	 * 			2D Transform
	 */
	public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {

		double[] destPoints = new double[sky.planetPositions().length];
		int size = destPoints.length/2;
		transform.transform2DPoints(sky.planetPositions(), 0, destPoints, 0, size);

		int y = 0;

		graphicsContext.setFill(Color.LIGHTGRAY);

		for (int i = 0; i<sky.planets().size(); ++i) {

			Planet tempPlanet = sky.planets().get(i);
			double planetSize = diskSize(projection, tempPlanet);
			Point2D planetDistance = transform.deltaTransform(0, planetSize); 
			double diameter = planetDistance.magnitude();

			/**
			 * Debugging
			 */
			//System.out.print(tempPlanet.name()+" "+diameter);	
			//System.out.println(" Planet X "+destPoints[y]+" Planet Y "+destPoints[y+1]);

			graphicsContext.fillOval(destPoints[y] - diameter/2, destPoints[y+1] - diameter/2, diameter, diameter);
			y+=2;
		}

		/**
		 * Debugging
		 */
		//canvas.getGraphicsContext2D().setStroke(Color.LIGHTGRAY);
	}

	/**
	 * Draw all the Stars
	 * 
	 * @param sky
	 * 			Observed Sky
	 * 
	 * @param projection
	 * 			Stereographic Projection
	 * 
	 * @param transform
	 * 			2D Transform
	 */
	public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform) {

		for(Star star : sky.stars()) {

			int temperature = star.colorTemperature();
			Color starColor = BlackBodyColor.colorForTemperature(temperature);

			double diameter = starRadius.get(star);
			double x = starTransformed.get(star).x();
			double y1 = starTransformed.get(star).y();

			graphicsContext.setFill(starColor);
			graphicsContext.fillOval(x - diameter/2, y1 - diameter/2, diameter, diameter);

		}
	}

	/**
	 * Draws the Sun
	 * 
	 * @param sky
	 * 			Observed sky
	 * 
	 * @param projection
	 * 			Stereographic Projection used
	 * 
	 * @param transform
	 * 			2D transform
	 * 
	 */
	public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform) {
		CartesianCoordinates sunCoor = sky.sunPosition();

		double sunSize = projection.applyToAngle(Angle.ofDeg(0.5));
		Point2D sunPoint = transform.transform(sunCoor.x(), sunCoor.y());
		double diameter = transform.deltaTransform(0, sunSize)
				.magnitude();
		double x = sunPoint.getX();
		double y = sunPoint.getY();

		//Sort ovals based on diameters		

		if (diameter < (2.0/1.2)) {	
			graphicsContext.setFill(Color.YELLOW);
			graphicsContext.fillOval(x - (2 + diameter)/2, y - (2 + diameter)/2, 2 + diameter, 2 + diameter);

			graphicsContext.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
			graphicsContext.fillOval(x - (2.2 * diameter)/2, y - (2.2 * diameter)/2, 2.2 * diameter, 2.2 * diameter);
		}

		else {
			graphicsContext.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
			graphicsContext.fillOval(x - (2.2 * diameter)/2, y - (2.2 * diameter)/2, 2.2 * diameter, 2.2 * diameter);

			graphicsContext.setFill(Color.YELLOW);
			graphicsContext.fillOval(x - (2 + diameter)/2, y - (2 + diameter)/2, 2 + diameter, 2 + diameter);
		}


		canvas.getGraphicsContext2D().setFill(Color.WHITE);
		canvas.getGraphicsContext2D().fillOval(x - diameter/2, y - diameter/2, diameter, diameter);

		/**
		 * Debugging
		 */
		//System.out.println("Sun diameter "+diameter);
		//System.out.println("Sun diameter 2 "+ 2.2*diameter);
		//System.out.println("Sun diameter 3 "+ diameter+2);
		//System.out.println("Sun X "+x);
		//System.out.println("Sun Y "+y);
	}

	/**
	 * Draws the Moon
	 * 
	 * @param sky
	 * 			Observed sky used
	 * 			
	 * @param projection
	 * 			Stereographic projection used
	 * 
	 * @param transform
	 * 			Transform used
	 * 
	 */
	public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform) {
		Moon moon = sky.moon();
		CartesianCoordinates moonCoor = sky.moonPosition();

		double moonSize = projection.applyToAngle(Angle.ofDeg(0.5));
		Point2D moonPoint = transform.transform(moonCoor.x(), moonCoor.y());
		double diameter = transform.deltaTransform(0, moonSize)
				.magnitude();
		double x = moonPoint.getX();
		double y = moonPoint.getY();

		canvas.getGraphicsContext2D().setFill(Color.WHITE);
		canvas.getGraphicsContext2D().fillOval(x - diameter/2, y - diameter/2, diameter, diameter);

		/**
		 * Debugging
		 */
		//Angular Size of the Moon or Angle.ofDeg(0.5) 
		//System.out.println("Moon diameter "+diameter);
		//System.out.println("Moon X "+x);
		//System.out.println("Moon Y "+y);
	}

	/**
	 * Draws the asterisms
	 * 
	 * @param sky
	 * 			Observed sky used
	 * 			
	 * @param projection
	 * 			Stereographic projection used
	 * 
	 * @param transform
	 * 			Transform used
	 * 			
	 */
	public void drawAsterisms(ObservedSky sky, StereographicProjection projection, Transform transform) {

		//Asterism trace
		//Translation not taken into account
		transformStars(sky, projection, transform);
		graphicsContext.setStroke(Color.BLUE);
		graphicsContext.setLineWidth(1.0);

		for(Asterism asterism : sky.asterisms()) {
			//System.out.println(asterism.stars());
			List<Integer> list = sky.asterismIndices(asterism);

			for(int i = 0; i < list.size()-1; ++i) {

				graphicsContext.beginPath();

				Star s1 = sky.stars().get(list.get(i));

				double d1 = starRadius.get(s1);
				double x1 = starPoints[2*(list.get(i))];
				double y1 = starPoints[2*(list.get(i)) + 1];

				Star s2 = sky.stars().get(list.get(i+1));

				double d2 = starRadius.get(s2);
				double x2 = starPoints[2*(list.get(i+1))];
				double y2 = starPoints[2*(list.get(i+1)) + 1];

				//System.out.println("Star 1 = "+s1.name() + " Star 2 = "+s2.name());

				if ((canvas.getBoundsInLocal().contains(x1, y1) 
						|| canvas.getBoundsInLocal().contains(x2, y2))) {

					//System.out.println("x1 "+x1+" y1 "+y1);
					//System.out.println("x2 "+x2+" y2 "+y2);	
					graphicsContext.moveTo(x1, y1);
					graphicsContext.lineTo(x2, y2);
					graphicsContext.stroke();
				}	
			}
		}
	}

	/**
	 * Transforms the stars positions of the sky given the projection and transform
	 *  
	 * @param sky
	 * 			Observed sky used
	 * 
	 * @param projection
	 * 			Stereographic projection used
	 * 
	 * @param transform
	 * 			Transform used
	 * 
	 */
	public void transformStars(ObservedSky sky, StereographicProjection projection, Transform transform) {
		//Stars
		starPoints = new double[sky.starPositions().length];


		transform.transform2DPoints(sky.starPositions(), 0, starPoints, 0, starPoints.length/2);
		int y = 0;

		//System.out.println(Arrays.toString(sky.starPositions()));
		//System.out.println(Arrays.toString(starPoints));

		for(Star star : sky.stars()) {
			starTransformed.put(star, CartesianCoordinates.of(starPoints[y], starPoints[y+1]));
			double starSize = diskSize(projection, star);
			Point2D starDistance = transform.deltaTransform(starSize, 0);
			double distance = starDistance.magnitude();
			starRadius.put(star, distance);
			y+=2;

			/**
			 * Debugging
			 */
			//System.out.println("Magnitude : "+distance);
			//System.out.println("Distance : "+starDistance.distance(0, 0));
			//double distance = starDistance.distance(0, 0);
			//System.out.println(distance);
		}
	}


	public void testPath(Transform transform) {
		canvas.getGraphicsContext2D().setStroke(Color.BLUE);
		canvas.getGraphicsContext2D().beginPath();
		canvas.getGraphicsContext2D().setLineWidth(1.0);

		Point2D origin = transform.transform(0.345, 0.223);
		canvas.getGraphicsContext2D().setLineWidth(1.0);
		canvas.getGraphicsContext2D().moveTo(origin.getX(), origin.getY());
		canvas.getGraphicsContext2D().lineTo(720.7839926018132, 368.9557635185101);
		canvas.getGraphicsContext2D().stroke();
		//canvas.getGraphicsContext2D().moveTo(origin.getX(), origin.getY());
		//canvas.getGraphicsContext2D().lineTo(327.45, 435.67);
		//canvas.getGraphicsContext2D().stroke();
	}

	/**
	 * Draws the horizon
	 * 
	 * @param sky
	 * 			Observed sky used
	 * 
	 * @param projection
	 * 			Stereographic projection used
	 * 
	 * @param transform
	 * 			Traansform used
	 * 
	 */
	public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform transform) {

		HorizontalCoordinates obs = HorizontalCoordinates.ofDeg(0, 0);
		CartesianCoordinates centerCoor = projection.circleCenterForParallel(obs);
		double radius = projection.circleRadiusForParallel(obs);
		Point2D centerPoint = transform.transform(centerCoor.x(), centerCoor.y());
		Point2D centerDistance = transform.deltaTransform(radius, 0);
		double transRadius = centerDistance.magnitude();

		canvas.getGraphicsContext2D().setStroke(Color.RED);
		canvas.getGraphicsContext2D().setLineWidth(2.0);
		canvas.getGraphicsContext2D().strokeOval(centerPoint.getX() - transRadius, centerPoint.getY() - transRadius, transRadius * 2, transRadius * 2);
		canvas.getGraphicsContext2D().setTextBaseline(VPos.TOP);

		canvas.getGraphicsContext2D().setFill(Color.RED);
		canvas.getGraphicsContext2D().setLineWidth(1.0);

		for(int i = 0; i < 8; ++i){
			HorizontalCoordinates cardinalHorizontal = HorizontalCoordinates.ofDeg(i * 45, -0.5);
			CartesianCoordinates projectedCardinal = projection.apply(cardinalHorizontal);
			Point2D transformedCardinalPoint = transform.transform(projectedCardinal.x(), projectedCardinal.y());

			String cardinalPointName = cardinalHorizontal.azOctantName(HorizontalCoordinates.NORTH_STRING, 
					HorizontalCoordinates.EAST_STRING, 
					HorizontalCoordinates.SOUTH_STRING, 
					HorizontalCoordinates.WEST_STRING);

			graphicsContext.fillText(cardinalPointName, transformedCardinalPoint.getX(), transformedCardinalPoint.getY());
		}

		/**
		 * Debugging
		 */
		 //String cardinal = obs.azOctantName("N", "E", "S", "W");

		//System.out.println(sky.observationPosition());
		//System.out.println(centerPoint.getX() - transRadius);
		//System.out.println(centerPoint.getY() - transRadius);
		//System.out.println(southPoint.getX());
		//System.out.println(transRadius * 2);
	}

	/**
	 * Computes the size of the disk of the celestial object
	 * 
	 * @param proj
	 * 			Stereographic projection used
	 * 
	 * @param object
	 * 			Celestial object
	 * 
	 * @return size of the disk of the celestial object
	 */
	private double diskSize(StereographicProjection proj, CelestialObject object) {

		double mp = MAGNITUDE_INT.clip(object.magnitude());	
		double factor = (99 - 17*mp)/ 140.0;

		//System.out.println(object.name());
		//System.out.println(object.magnitude());

		return factor * proj.applyToAngle(Angle.ofDeg(0.5));
	}
}
