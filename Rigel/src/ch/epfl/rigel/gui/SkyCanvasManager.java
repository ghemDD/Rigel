package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import static java.lang.Math.abs;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * Manages the canvas
 * 
 * @author Nael Ouerghemi (310435)
 */
public class SkyCanvasManager {

	//Canvas
	private final Canvas canvas;

	//Internal links
	private final ObjectBinding<StereographicProjection> projection;
	private final ObjectBinding<Transform> planeToCanvas;
	private final ObjectBinding<ObservedSky> observedSky;
	private final ObjectProperty<CartesianCoordinates> mousePosition;
	private final ObjectBinding<CartesianCoordinates> inverseTransformedMouse;
	private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

	//External links
	public final ObjectBinding<Double> mouseAzDeg;
	public final ObjectBinding<Double> mouseAltDeg;
	public final ObjectBinding<CelestialObject> objectUnderMouse;

	//Intervals
	private static final ClosedInterval ALT_INT = ClosedInterval.of(5, 90);
	private static final RightOpenInterval LON_INT = RightOpenInterval.of(0, 360);
	private static final ClosedInterval FOV_INT = ClosedInterval.of(30, 150);

	//Incrementations/Decrementations
	private static final double ALT_INC = 5.0;
	private static final double LON_INC = 10.0;
	
	private static final double MAX_DISTANCE = 10.0;

	/**
	 * Constructor of the skyCanvasManager
	 * 
	 * @param catalogue
	 * 			Star catalogue
	 * 
	 * @param dateTimeBean
	 * 			Bean containing date information
	 * 
	 * @param observerLocationBean
	 * 			Bean containing observer's location information
	 * 
	 * @param viewingParametersBean
	 * 			Bean containing the conditions of observation
	 * 
	 */
	public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
		canvas = new Canvas();
		SkyCanvasPainter painter = new SkyCanvasPainter(canvas);

		//Bindings

		projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenterCoordinates()), 
				viewingParametersBean.getCenterCoordinatesProperty());

		observedSky = Bindings.createObjectBinding(
				() -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getGeographicCoordinates(), projection.get(), catalogue), 
				dateTimeBean.getZonedDateTimeProperty(), observerLocationBean.getGeographicCoordinatesBinding(), projection);

		DoubleBinding dilatation = Bindings.createDoubleBinding(
				() -> (dilatationFactor(projection.get(), viewingParametersBean.getFieldOfViewDeg())), 
				projection, viewingParametersBean.getFieldOfViewDegProperty(), canvas.widthProperty());

		planeToCanvas = Bindings.createObjectBinding(() -> {
			Transform scale = Transform.scale(dilatation.get(), - dilatation.get());
			Transform translate = Transform.translate(canvas.getWidth()/2, canvas.getHeight()/2);

			return translate.createConcatenation(scale);

		}, dilatation, canvas.widthProperty(), canvas.heightProperty());

		//Initialization Mouse Position
		mousePosition = new SimpleObjectProperty<CartesianCoordinates>();
		mousePosition.set(CartesianCoordinates.of(0, 0));


		inverseTransformedMouse = Bindings.createObjectBinding(() -> {
			try {
				Point2D invertedMousePosition = planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
				return CartesianCoordinates.of(invertedMousePosition.getX(), invertedMousePosition.getY());
			}	catch (NonInvertibleTransformException e){
				return CartesianCoordinates.of(0, 0);
			}

		}, mousePosition, planeToCanvas);

		objectUnderMouse = Bindings.createObjectBinding(
				() -> {
					try {
						CelestialObject object = observedSky.get().objectClosestTo(inverseTransformedMouse.get(), MAX_DISTANCE).get();
						return object;
					}	catch (NullPointerException e) {
						return null;
					}
					 
				}, observedSky, inverseTransformedMouse);

		mouseHorizontalPosition = Bindings.createObjectBinding(
				() -> projection.get().inverseApply(inverseTransformedMouse.get()), 
				projection, inverseTransformedMouse);

		mouseAzDeg = Bindings.createObjectBinding(
				() -> mouseHorizontalPosition.get().azDeg(), 
				mouseHorizontalPosition);

		mouseAltDeg = Bindings.createObjectBinding(
				() -> mouseHorizontalPosition.get().altDeg(), 
				mouseHorizontalPosition);

		//Listener keyboard 
		canvas.setOnKeyPressed( (event) -> {
			canvas.requestFocus();
			double lon = viewingParametersBean.getCenterLonDeg();
			double alt = viewingParametersBean.getCenterAltDeg();

			switch(event.getCode()) {
			case LEFT :
				lon -= LON_INC;
				lon = LON_INT.reduce(lon);

				viewingParametersBean.setCenterLonDeg(lon);
				break;

			case RIGHT :
				lon += LON_INC;
				lon = LON_INT.reduce(lon);

				viewingParametersBean.setCenterLonDeg(lon);
				break;

			case UP : 
				alt += ALT_INC;
				alt = ALT_INT.clip(alt);

				viewingParametersBean.setCenterAltDeg(alt);
				break;

			case DOWN : 
				alt -= ALT_INC;
				alt = ALT_INT.clip(alt);

				viewingParametersBean.setCenterAltDeg(alt);
				break;

			default:
				break;
			}

			event.consume();
		});

		//Listeners mouse
		canvas.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown())
				canvas.requestFocus();
		});

		canvas.setOnMouseMoved((event) -> {
			double x = event.getX();
			double y = event.getY();

			mousePosition.set(CartesianCoordinates.of(x, y));
		});

		canvas.setOnScroll((event) -> {
			double fov = viewingParametersBean.getFieldOfViewDeg();
			double x = event.getDeltaX();
			double y = event.getDeltaY();
			double max = abs(x) >= abs(y) ? x : y; 

			fov = FOV_INT.clip(fov + max);
			viewingParametersBean.setFieldOfViewDeg(fov);
		});

		//Listeners painters
		canvas.widthProperty().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));

		canvas.heightProperty().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));

		dateTimeBean.getZonedDateTimeProperty().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));

		observerLocationBean.getGeographicCoordinatesBinding().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));

		viewingParametersBean.getFieldOfViewDegProperty().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));

		viewingParametersBean.getCenterCoordinatesProperty().addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
	}

	/**
	 * Computes dilatation factor given the dimensions of the canvas and field of view
	 * 
	 * @param proj
	 * 			Stereographic projection used
	 * 
	 * @param fov
	 * 			Field of view (in degrees)
	 * 
	 * @return dilatation factor given the dimensions of the canvas and field of view
	 */
	private double dilatationFactor(StereographicProjection proj, double fov) {
		return (canvas.getWidth())/(proj.applyToAngle(Angle.ofDeg(fov)));
	}

	/**
	 * Getter for the canvas
	 * 
	 * @return canvas managed
	 */
	public Canvas canvas() {return canvas;}

	/**
	 * Getter for objectUnderMouse property
	 * 
	 * @return binding objectUnderMouse
	 */
	public ObjectBinding<CelestialObject> objectUnderMouseProperty() {return objectUnderMouse;}

	/**
	 * Getter for the mouse longitude property
	 * 
	 * @return mouse longitude property
	 */
	public ObjectBinding<Double> mouseAzDegProperty() {return mouseAzDeg;}

	/**
	 * Getter for the mouse altitude property
	 * 
	 * @return mouse altitude property
	 */
	public ObjectBinding<Double> mouseAltDegProperty() {return mouseAltDeg;}
}
