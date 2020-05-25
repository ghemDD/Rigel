package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
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
import javafx.scene.transform.Transform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Paints the sky on the canvas
 * 
 * @author Nael Ouerghemi (310435)
 */
public class SkyCanvasPainter {

	private final Canvas canvas;
	private final GraphicsContext graphicsContext;
	private static final ClosedInterval MAGNITUDE_INT = ClosedInterval.of(-2, 5);
	private final Map<Star, CartesianCoordinates> starTransformed;
	private final Map<Star, Double> starRadius;
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

	public void clear(Color color) {
		graphicsContext.setFill(color);
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
	public void drawSky(ObservedSky sky, StereographicProjection projection, Transform transform, Color color) {	
		clear(color);
		transformStars(sky, projection, transform);
		drawAsterisms(sky, projection, transform);
		drawStars(sky, projection, transform);
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

			graphicsContext.fillOval(destPoints[y] - diameter/2, destPoints[y+1] - diameter/2, diameter, diameter);
			y+=2;
		}
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
			double y = starTransformed.get(star).y();

			graphicsContext.setFill(starColor);
			graphicsContext.fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
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
		double[] diameters = new double[3];
		Color[] colors = new Color[3];

		//Sort ovals based on diameters
		// 2 + diameter > 2.2 * diameter <=> diameter < (2.0/1.2)

		if (diameter < (2.0/1.2)) {	
			colors[0] = Color.YELLOW;
			diameters[0] = 2 + diameter;

			colors[1] = Color.YELLOW.deriveColor(1, 1, 1, 0.25);
			diameters[1] = 2.2 * diameter;	
		}

		else {
			colors[0] = Color.YELLOW.deriveColor(1, 1, 1, 0.25);
			diameters[0] = 2.2 * diameter;

			colors[1] = Color.YELLOW;
			diameters[1] = 2 + diameter;
		}

		colors[2] = Color.WHITE;
		diameters[2] = diameter;

		for(int i = 0; i < diameters.length; ++i) {
			canvas.getGraphicsContext2D().setFill(colors[i]);
			canvas.getGraphicsContext2D().fillOval(x - diameters[i]/2, y - diameters[i]/2, diameters[i], diameters[i]);
		}
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
		//Moon moon = sky.moon();
		CartesianCoordinates moonCoor = sky.moonPosition();

		//Moon size based on method diskSize or applyToAngle ?
		double moonSize = projection.applyToAngle(Angle.ofDeg(0.5));
		Point2D moonPoint = transform.transform(moonCoor.x(), moonCoor.y());
		double diameter = transform.deltaTransform(0, moonSize)
				.magnitude();
		double x = moonPoint.getX();
		double y = moonPoint.getY();

		canvas.getGraphicsContext2D().setFill(Color.WHITE);
		canvas.getGraphicsContext2D().fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
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

		graphicsContext.setStroke(Color.BLUE);
		graphicsContext.setLineWidth(1.0);

		for(Asterism asterism : sky.asterisms()) {
			List<Integer> list = sky.asterismIndices(asterism);

			for(int i = 0; i < list.size()-1; ++i) {

				graphicsContext.beginPath();

				double x1 = starPoints[2*(list.get(i))];
				double y1 = starPoints[2*(list.get(i)) + 1];

				double x2 = starPoints[2*(list.get(i+1))];
				double y2 = starPoints[2*(list.get(i+1)) + 1];

				if ((canvas.getBoundsInLocal().contains(x1, y1) || canvas.getBoundsInLocal().contains(x2, y2))) {

					graphicsContext.moveTo(x1, y1);
					graphicsContext.lineTo(x2, y2);
					graphicsContext.stroke();
				}
			}

			graphicsContext.closePath();
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

		starPoints = new double[sky.starPositions().length];
		transform.transform2DPoints(sky.starPositions(), 0, starPoints, 0, starPoints.length/2);
		int y = 0;

		for(Star star : sky.stars()) {
			double starSize = diskSize(projection, star);
			Point2D starDistance = transform.deltaTransform(starSize, 0);
			double diameter = starDistance.magnitude();
			starTransformed.put(star, CartesianCoordinates.of(starPoints[y], starPoints[y+1]));
			starRadius.put(star, diameter);
			y+=2;
		}
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

		return factor * proj.applyToAngle(Angle.ofDeg(0.5));
	}
}
