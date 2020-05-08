package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ViewingParametersBean {
	private ObjectProperty<Double> fieldOfViewDeg; 
	private ObjectProperty<HorizontalCoordinates> centerCoordinates;

	/**
	 * 
	 */
	public ViewingParametersBean() {
		fieldOfViewDeg = new SimpleObjectProperty<Double>();
		centerCoordinates = new SimpleObjectProperty<HorizontalCoordinates>();
	}

	/**
	 * 
	 * @param fov
	 */
	public void setFieldOfViewDeg(double fov) {
		fieldOfViewDeg.set(fov);
	}

	/**
	 * 
	 * @return
	 */
	public double getFieldOfViewDeg() {
		return fieldOfViewDeg.get();
	}

	/**
	 * 
	 * @return
	 */
	public ObjectProperty<Double> getFieldOfViewDegProperty() {
		return fieldOfViewDeg;
	}

	/**
	 * 
	 * @param hor
	 */
	public void setCenter(HorizontalCoordinates hor) {
		centerCoordinates.set(hor);
	}

	/**
	 * 
	 * @return
	 */
	public double getCenterLonDeg() {
		return centerCoordinates.get().azDeg();
	}

	/**
	 * 
	 * @param longitude
	 */
	public void setCenterLonDeg(double longitude) {
		HorizontalCoordinates hor = HorizontalCoordinates.ofDeg(longitude, centerCoordinates.get().altDeg());
		centerCoordinates.set(hor);
	}

	/**
	 * 
	 * @return
	 */
	public double getCenterAltDeg() {
		return centerCoordinates.get().altDeg();
	}

	/**
	 * 
	 * @param alt
	 */
	public void setCenterAltDeg(double alt) {
		HorizontalCoordinates hor = HorizontalCoordinates.ofDeg(centerCoordinates.get().azDeg(), alt);
		centerCoordinates.set(hor);
	}

	/**
	 * 
	 * @return
	 */
	public HorizontalCoordinates getCenterCoordinates() {
		return centerCoordinates.get();
	}

	/**
	 * 
	 * @return
	 */
	public ObjectProperty<HorizontalCoordinates> getCenterCoordinatesProperty() {
		return centerCoordinates;
	}
}
