package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import static java.lang.Math.abs;

import java.util.Optional;

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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
	private final ObjectProperty<CartesianCoordinates> mousePositionOnPressed;
	private final ObjectBinding<CartesianCoordinates> inverseTransformedMouse;
	private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
	private final BooleanProperty tracePath;
	private final BooleanProperty clearPath;
	private final StringProperty selectedStar;

	private final BooleanProperty showStars;
	private final BooleanProperty showAsterisms;
	private final BooleanProperty showHorizon;
	private final BooleanProperty showGrid;
	private final BooleanProperty showEcliptic;
	private final BooleanProperty showEquator;


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
		selectedStar = new SimpleStringProperty();

		showGrid = new SimpleBooleanProperty();

		showStars = new SimpleBooleanProperty();
		showAsterisms = new SimpleBooleanProperty();
		showHorizon = new SimpleBooleanProperty();

		showEcliptic = new SimpleBooleanProperty();
		showEquator = new SimpleBooleanProperty();

		//Bindings
		painter.showGridProperty().bind(showGrid);
		painter.showAsterismsProperty().bind(showAsterisms);
		painter.showHorizonProperty().bind(showHorizon);
		painter.showStarsProperty().bind(showStars);
		painter.showEclipticProperty().bind(showEcliptic);
		painter.showEquatorProperty().bind(showEquator);


		projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenterCoordinates()), 
				viewingParametersBean.centerCoordinatesProperty());

		observedSky = Bindings.createObjectBinding(
				() -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getGeographicCoordinates(), projection.get(), catalogue, getSelectedStar()), 
				dateTimeBean.zonedDateTimeProperty(), observerLocationBean.getGeographicCoordinatesBinding(), projection);

		DoubleBinding dilatation = Bindings.createDoubleBinding(
				() -> (dilatationFactor(projection.get(), viewingParametersBean.getFieldOfViewDeg())), 
				projection, viewingParametersBean.fieldOfViewDegProperty(), canvas.widthProperty());

		planeToCanvas = Bindings.createObjectBinding(() -> {
			Transform scale = Transform.scale(dilatation.get(), - dilatation.get());
			Transform translate = Transform.translate(canvas.getWidth()/2, canvas.getHeight()/2);

			return translate.createConcatenation(scale);

		}, dilatation, canvas.widthProperty(), canvas.heightProperty());

		//Initialization Mouse Position
		mousePosition = new SimpleObjectProperty<CartesianCoordinates>();
		mousePosition.set(CartesianCoordinates.of(0, 0));

		tracePath = new SimpleBooleanProperty();
		clearPath = new SimpleBooleanProperty();


		inverseTransformedMouse = Bindings.createObjectBinding(() -> {
			try {
				Point2D invertedMousePosition = planeToCanvas.get()
						.inverseTransform(mousePosition.get().x(), mousePosition.get().y());

				return CartesianCoordinates.of(invertedMousePosition.getX(), invertedMousePosition.getY());

			}	catch (NonInvertibleTransformException e){
				return CartesianCoordinates.of(0, 0);
			}

		}, mousePosition, planeToCanvas);

		objectUnderMouse = Bindings.createObjectBinding(
				() -> {
					try {
						double transformedMax = planeToCanvas.get()
								.inverseDeltaTransform(MAX_DISTANCE, 0)
								.magnitude();

						Optional<CelestialObject> object = observedSky.get()
								.objectClosestTo(inverseTransformedMouse.get(), transformedMax);

						return object.orElse(null);

					}	catch (Exception e) {
						return null;
					}

				}, observedSky, inverseTransformedMouse, planeToCanvas);

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

			if (!tracePath.get()) {

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
			}

			event.consume();
		});

		//Listeners mouse
		mousePositionOnPressed = new SimpleObjectProperty<CartesianCoordinates>();
		mousePositionOnPressed.set(CartesianCoordinates.of(0, 0));

		canvas.setOnMousePressed((event) -> {
			if (event.isPrimaryButtonDown())
				canvas.requestFocus();

			//Bonus
			mousePositionOnPressed.set(CartesianCoordinates.of(event.getX(), event.getY()));
		});

		canvas.setOnMouseMoved((event) -> {
			double x = event.getX();
			double y = event.getY();

			mousePosition.set(CartesianCoordinates.of(x, y));
		});


		/**
		 * Bonus 
		 */
		canvas.setOnMouseDragged((event) -> {

			double lon = viewingParametersBean.getCenterLonDeg();
			double alt = viewingParametersBean.getCenterAltDeg();

			if (!tracePath.get()) {
				double deltaX = mousePositionOnPressed.get().x() - event.getX();
				double deltaY = mousePositionOnPressed.get().y() - event.getY();

				lon += deltaX/2;
				lon = LON_INT.reduce(lon);

				alt += -deltaY/2;
				alt = ALT_INT.clip(alt);

				mousePositionOnPressed.set(CartesianCoordinates.of(event.getX(), event.getY()));
				viewingParametersBean.setCenterAltDeg(alt);
				viewingParametersBean.setCenterLonDeg(lon);
			}

			event.consume();
		});

		/**
		 * 
		 */

		canvas.setOnScroll((event) -> {
			double fov = viewingParametersBean.getFieldOfViewDeg();
			double x = event.getDeltaX();
			double y = event.getDeltaY();
			double max = abs(x) >= abs(y) ? x : y; 

			fov = FOV_INT.clip(fov + max);
			viewingParametersBean.setFieldOfViewDeg(fov);
		});

		//Listeners painters
		planeToCanvas.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		observedSky.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		clearPath.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showStars.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showAsterisms.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showHorizon.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showGrid.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showEquator.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

		showEcliptic.addListener(
				(o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get(), tracePath.get(), clearPath.get()));

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


	/**
	 * Getter for the value of trace path property
	 * 
	 * @return value of trace path property
	 */
	public boolean getTracePath() {return tracePath.get();}

	/**
	 * Setter for trace path property
	 * 
	 * @param value to be set in the property
	 */
	public void setTracePath(boolean value) {
		tracePath.set(value);
	}

	/**
	 * Getter for the value of clear path property
	 * 
	 * @return value of clear path property
	 */
	public boolean getClearPath() {return clearPath.get();}

	/**
	 * Setter for clear path property
	 * 
	 * @param value to be set in the property
	 */
	public void setClearPath(boolean value) {
		clearPath.set(value);
	}

	/**
	 * Getter for the value of the selected star property
	 * 
	 * @return value of the selected star property
	 */
	public String getSelectedStar() {
		return selectedStar.get();
	}

	/**
	 * Setter for the selected star property
	 * 
	 * @param value to be set in the property
	 */
	public void setSelectedStar(String value) {
		selectedStar.set(value);
	}

	/**
	 * Getter for the selected star property
	 * 
	 * @return selected star property
	 */
	public StringProperty getSelectedStarProperty() {
		return selectedStar;
	}

	/**
	 * Getter for the value of the show stars property
	 * 
	 * @return value of the show stars property
	 */
	public BooleanProperty showStarsProperty() {
		return showStars;
	}

	/**
	 * Getter for the value of the show horizon property
	 * 
	 * @return value of the show horizon property
	 */
	public BooleanProperty showHorizonProperty() {
		return showHorizon;
	}

	/**
	 * Getter for the value of the show asterisms property
	 * 
	 * @return value of the show asterisms property
	 */
	public BooleanProperty showAsterismsProperty() {
		return showAsterisms;
	}	

	/**
	 * Getter for the value of the show grid property
	 * 
	 * @return value of the show grid property
	 */
	public BooleanProperty showGridProperty() {
		return showGrid;
	}	

	/**
	 * Getter for the value of the show ecliptic property
	 * 
	 * @return value of the show ecliptic property
	 */
	public BooleanProperty showEclipticProperty() {
		return showEcliptic;
	}	

	/**
	 * Getter for the value of the show equator property
	 * 
	 * @return value of the show equator property
	 */
	public BooleanProperty showEquatorProperty() {
		return showEquator;
	}	
}
