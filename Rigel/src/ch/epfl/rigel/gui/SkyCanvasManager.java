package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

public class SkyCanvasManager {

	private Canvas canvas;

	//Internal links
	private ObjectBinding<StereographicProjection> projection;
	private ObjectBinding<Transform> planeToCanvas;
	private ObjectBinding<ObservedSky> observedSky;
	private ObjectProperty<CartesianCoordinates> mousePosition;
	private ObjectBinding<CartesianCoordinates> inverseTransformedMouse;
	private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

	//External links
	public ObjectBinding<Double> mouseAzDeg;
	public ObjectBinding<Double> mouseAltDeg;
	public ObjectBinding<CelestialObject> objectUnderMouse;

	private static final ClosedInterval ALT_INT = ClosedInterval.of(5, 90);
	private static final ClosedInterval FOV_INT = ClosedInterval.of(30, 150);

	public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
		//Modularisation constructor
		canvas = new Canvas(800, 600);
		SkyCanvasPainter painter = new SkyCanvasPainter(canvas);

		//Bindings

		projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenterCoordinates()), viewingParametersBean.getCenterCoordinatesProperty());

		observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getGeographicCoordinates(), projection.get(), catalogue), 
				dateTimeBean.getZonedDateTimeProperty(), observerLocationBean.getGeographicCoordinatesBinding(), projection);

		DoubleBinding dilatation = Bindings.createDoubleBinding(() -> (dilatationFactor(projection.get(), viewingParametersBean.getFieldOfViewDeg())), 
				projection, viewingParametersBean.getFieldOfViewDegProperty());

		System.out.println("Dilatation "+dilatation.get());

		planeToCanvas = Bindings.createObjectBinding(() -> {
			Transform scale = Transform.scale(dilatation.get(), -dilatation.get());
			Transform translate = Transform.translate(canvas.getWidth()/2, canvas.getHeight()/2);

			//return Transform.affine(dilatation.get(), 0, 0, -dilatation.get(), canvas.getWidth()/2, canvas.getHeight()/2);
			return translate.createConcatenation(scale);
		}, dilatation);

		//Initialization Mouse Position
		mousePosition = new SimpleObjectProperty<CartesianCoordinates>();
		mousePosition.set(CartesianCoordinates.of(0, 0));

		inverseTransformedMouse = Bindings.createObjectBinding(() -> {
			double mousePositionX = mousePosition.get().x();
			double mousePositionY = mousePosition.get().y();

			Point2D invertedMousePosition = planeToCanvas.get().inverseTransform(mousePositionX, mousePositionY);

			return CartesianCoordinates.of(invertedMousePosition.getX(), invertedMousePosition.getY());
		}, mousePosition, planeToCanvas);



		objectUnderMouse = Bindings.createObjectBinding(() -> observedSky.get().objectClosestTo(inverseTransformedMouse.get(), 10).get(), 
				observedSky, inverseTransformedMouse);

		mouseHorizontalPosition = Bindings.createObjectBinding(() -> projection.get().inverseApply(inverseTransformedMouse.get()), 
				projection, inverseTransformedMouse);

		mouseAzDeg = Bindings.createObjectBinding(() -> mouseHorizontalPosition.get().azDeg(), 
				mouseHorizontalPosition);

		mouseAltDeg = Bindings.createObjectBinding(() -> mouseHorizontalPosition.get().altDeg(), 
				mouseHorizontalPosition);

		//Listener keyboard 
		canvas.setOnKeyPressed( (event) -> {
			canvas.requestFocus();
			double lon = viewingParametersBean.getCenterLonDeg();
			double alt = viewingParametersBean.getCenterAltDeg();

			switch(event.getCode()) {
			case LEFT :
				//Use reduce of the right-open interval class
				lon -= 10;
				lon = lon < 0 ? lon + 360.0 : lon;

				viewingParametersBean.setCenterLonDeg(lon);
				break;

			case RIGHT :
				//Use reduce of the right-open interval class
				lon += 10;
				lon = lon >= 360.0 ? lon - 360.0 : lon;

				viewingParametersBean.setCenterLonDeg(lon);
				break;

			case UP : 
				alt +=5;
				alt = ALT_INT.clip(alt);

				viewingParametersBean.setCenterAltDeg(alt);
				break;

			case DOWN : 
				alt -= 5;
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
			//Stack in property to compute the closest celestial object
			double x = event.getX();
			double y = event.getX();

			mousePosition.set(CartesianCoordinates.of(x, y));
		});

		canvas.setOnScroll( (event) -> {
			double fov = viewingParametersBean.getFieldOfViewDeg();
			double x = event.getDeltaX();
			double y = event.getDeltaY();
			double max = Math.abs(x) >= Math.abs(y) ? x : y;

			fov = FOV_INT.clip(fov + max);
			viewingParametersBean.setFieldOfViewDeg(fov);
		});

		painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get());

		//Listeners preconditions
		canvas.widthProperty().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
		canvas.heightProperty().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
		dateTimeBean.getZonedDateTimeProperty().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
		inverseTransformedMouse.addListener((o) ->  {
			//System.out.println("Mouse Position "+inverseTransformedMouse.get());
		});
		observerLocationBean.getGeographicCoordinatesBinding().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
		viewingParametersBean.getFieldOfViewDegProperty().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
		viewingParametersBean.getCenterCoordinatesProperty().addListener((o) -> painter.drawSky(observedSky.get(), projection.get(), planeToCanvas.get()));
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
	public Canvas canvas() {
		return canvas;
	}

	/**
	 * Getter for objectUnderMouse property
	 * 
	 * @return binding objectUnderMouse
	 */
	public ObjectBinding<CelestialObject> objectUnderMouseProperty() {return objectUnderMouse;}
}

