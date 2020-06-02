package ch.epfl.rigel.gui;

import static ch.epfl.rigel.gui.BlackBodyColor.colorForTemperature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

/**
 * Paints the sky on the canvas
 * 
 * @author Nael Ouerghemi (310435)
 * @author Tanguy Marbot (316756)
 *
 */
public class SkyCanvasPainter {

	//Canvas
	private final Canvas canvas;

	private final GraphicsContext graphicsContext;

	//Star transform
	private final Map<Star, CartesianCoordinates> starTransformed;
	private final Map<Star, Double> starRadius;
	private double[] starPoints;

	private final static HorizontalCoordinates ZERO_LAT_COORDINATES = HorizontalCoordinates.of(0.0,0.0);
	private final static List<HorizontalCoordinates> CARDINALPOINTS_LIST= new ArrayList<HorizontalCoordinates>();

	private static final ClosedInterval MAGNITUDE_INT = ClosedInterval.of(-2, 5);
	
	private boolean tracePath;
	private List<Double> deltaXPath;
	private List<Double> deltaYPath;
	private CartesianCoordinates firstCoor;
	private boolean first;
	
	private BooleanProperty showStars;
	private BooleanProperty showAsterisms;
	private BooleanProperty showHorizon;
	

	/**
	 * Constructor for skyCanvasPainter
	 * 
	 * @param canvas
	 * 			Canvas on which the celestial objects are printed 
	 */
	public SkyCanvasPainter(Canvas canvas) {
		this.canvas = canvas;
		graphicsContext = this.canvas.getGraphicsContext2D();
		starTransformed = new HashMap<Star, CartesianCoordinates>();
		starRadius = new HashMap<Star, Double>();
		deltaXPath = new ArrayList<Double>();
		deltaYPath = new ArrayList<Double>();
		first = true;
		
		showStars = new SimpleBooleanProperty();
		showAsterisms = new SimpleBooleanProperty();
		showHorizon = new SimpleBooleanProperty();

		// we initiate the cardinal points list
		for(int i = 0; i < 8; i++) {
			CARDINALPOINTS_LIST.add(HorizontalCoordinates.ofDeg((double) i * 45, -0.5));
		}
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
	public void drawSky(ObservedSky sky, StereographicProjection projection, Transform transform, boolean tracePath, boolean clearPath) {	
		clear();
		this.tracePath = tracePath;
		transformStars(sky, projection, transform);
		
		if (showAsterisms.get())
			drawAsterisms(sky, projection, transform);
		
		if (showStars.get())
			drawStars(sky, projection, transform);
		
		drawPlanets(sky, projection, transform);
		drawSun(sky, projection, transform);
		drawMoon(sky, projection, transform);
		
		if (showHorizon.get())
			drawHorizon(sky, projection, transform);
		
		if (clearPath) {
			deltaXPath.clear();
			deltaYPath.clear();
		}
		
		tracePath(sky, transform);
	}

	/**
	 * Clears the canvas by filling with black rectangle adapted to the dimensions of the canvas
	 */
	public void clear() {
		graphicsContext.setFill(Color.BLACK);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

		int index = 0;

		graphicsContext.setFill(Color.LIGHTGRAY);

		for (Planet planet : sky.planets()) { 
			double diameter = transformedDiskSize(projection, planet.magnitude(), transform);
			graphicsContext.fillOval(destPoints[index] - diameter/2, destPoints[index+1] - diameter/2, diameter, diameter);
			index+=2;
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
			Color starColor = colorForTemperature(temperature);

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
	 */
	public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform) {
		CartesianCoordinates sunCoor = sky.sunPosition();
		double sunSize = projection.applyToAngle(Angle.ofDeg(0.5));
		Point2D sunPoint = transform.transform(sunCoor.x(), sunCoor.y());
		double diameter = transform.deltaTransform(0, sunSize)
				.magnitude();

		double x = sunPoint.getX();
		double y = sunPoint.getY();

		//Here the index correspond to the order of painting, index 0 being the background
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
		CartesianCoordinates moonCoor = sky.moonPosition();

		double moonSize = projection.applyToAngle(Angle.ofDeg(0.5));
		Point2D moonPoint = transform.transform(moonCoor.x(), moonCoor.y());
		double diameter = transform.deltaTransform(0, moonSize)
				.magnitude();
		double x = moonPoint.getX();
		double y = moonPoint.getY();

		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
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
	 * This method is made to separate the painting of asterims and stars
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
	private void transformStars(ObservedSky sky, StereographicProjection projection, Transform transform) {
		starPoints = new double[sky.starPositions().length];
		transform.transform2DPoints(sky.starPositions(), 0, starPoints, 0, starPoints.length/2);
		int y = 0;
		

		for(Star star : sky.stars()) {
			double diameter = transformedDiskSize(projection, star.magnitude(), transform);
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
	 * 			Transform used
	 */
	public void drawHorizon(ObservedSky sky, StereographicProjection projection, Transform transform) {

		final CartesianCoordinates horizonCircleCenter= projection.circleCenterForParallel(ZERO_LAT_COORDINATES);
		double horizonCircleRadius = projection.circleRadiusForParallel(ZERO_LAT_COORDINATES);

		Point2D transfHorCircleCenterPos= transform.
				transform(horizonCircleCenter.x(), horizonCircleCenter.y() );

		horizonCircleRadius = transform.deltaTransform(0, horizonCircleRadius).magnitude();

		double diameter = horizonCircleRadius*2;
		// Here we draw the Horizon
		graphicsContext.setLineWidth(2);
		graphicsContext.setStroke(Color.RED);
		graphicsContext.strokeOval(transfHorCircleCenterPos.getX() - diameter/2, transfHorCircleCenterPos.getY() - diameter/2, diameter, diameter);
		graphicsContext.fill();

		CartesianCoordinates tempCardTextPos;
		Point2D tempTransCardTextPos;
		String tempText;

		for( HorizontalCoordinates tempCoord : CARDINALPOINTS_LIST) {
			graphicsContext.setFill(Color.RED);
			graphicsContext.setTextAlign(TextAlignment.CENTER);
			graphicsContext.setTextBaseline(VPos.TOP);
			tempText= tempCoord.azOctantName("N", "E", "S", "O");
			tempCardTextPos = projection.apply(tempCoord);
			tempTransCardTextPos = transform.transform(tempCardTextPos.x(),  tempCardTextPos.y());
			graphicsContext.fillText(tempText, tempTransCardTextPos.getX(), tempTransCardTextPos.getY());
		}
	}
	
	
	private void tracePath(ObservedSky sky, Transform transform) {
		CartesianCoordinates actual;
		actual = sky.pathCoordinates();
		
		if (first && tracePath) {
			firstCoor = sky.pathCoordinates();
			first = false;
		}
		
		else if (!tracePath) {
			firstCoor = sky.pathCoordinates();
		}
		
		else {
			if (tracePath) {
				deltaXPath.add(actual.x() - firstCoor.x());
				deltaYPath.add(actual.y() - firstCoor.y()); 
			}
		}
		
		for(int i = 0; i < deltaXPath.size(); ++i) {
			//System.out.println("RIGEL "+deltaXPath.size());
			//System.out.println("DELTA X "+deltaXPath.get(i));
			//System.out.println("DELTA Y "+deltaYPath.get(i));
			//System.out.println("Rigel actual "+actual);
			
			Point2D point = transform.transform(actual.x() - deltaXPath.get(i), actual.y() - deltaYPath.get(i));
			graphicsContext.setFill(Color.RED);
			graphicsContext.fillOval(point.getX() - 0.5, point.getY() - 0.5, 1, 1);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public BooleanProperty getShowStarsProperty() {
		return showStars;
	}
	
	/**
	 * 
	 * @return
	 */
	public BooleanProperty getShowAsterismsProperty() {
		return showAsterisms;
	}
	
	/**
	 * 
	 * @return
	 */
	public BooleanProperty getShowHorizonProperty() {
		return showHorizon;
	}
	
	/**
	 * Computes the size of the disk of the transformed celestial object
	 * Method used for celestial objects whose positions are stored in arrays, as it would duplicate the transform operation of the coordinates for a single celestial object
	 * 
	 * @param proj
	 * 			Stereographic projection used
	 * 
	 * @param magnitude
	 * 			Magnitude of the celestial object
	 * 
	 * @param transform
	 * 			Transform depending on the canvas
	 * 
	 * @return size of the disk of the transformed celestial object
	 */
	private double transformedDiskSize(StereographicProjection proj, double magnitude, Transform transform) {

		double mp = MAGNITUDE_INT.clip(magnitude);	
		double factor = (99 - 17*mp)/ 140.0;
		double size = factor * proj.applyToAngle(Angle.ofDeg(0.5));
		Point2D transformedDistance = transform.deltaTransform(size, 0);

		return transformedDistance.magnitude();
	}
	
}
