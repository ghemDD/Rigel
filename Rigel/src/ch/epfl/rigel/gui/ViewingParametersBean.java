package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Bean containing the informations of the conditions of observation
 * 
 * @author Tanguy Marbot (316756)
 */
public class ViewingParametersBean {

	private final DoubleProperty fieldOfViewDeg; 
	private final ObjectBinding<HorizontalCoordinates> centerCoordinates;
	private final DoubleProperty lonDeg;
	private final DoubleProperty altDeg;

	/**
	 * Constructor for the viewingParametersBean : initialises properties fieldOfViewDeg, lonDeg, altDeg properties and binding centerCoordinates
	 */
	public ViewingParametersBean() {
		fieldOfViewDeg = new SimpleDoubleProperty();
		lonDeg = new SimpleDoubleProperty();
		altDeg = new SimpleDoubleProperty();
		centerCoordinates = Bindings.createObjectBinding(
				() -> HorizontalCoordinates.ofDeg(lonDeg.getValue(), altDeg.getValue()), 
				lonDeg, altDeg);
	}

	//FIELD OF VIEW PROPERTY

	/**
	 * Setter for the field of view property
	 * 
	 * @param fov
	 * 		Field of view to be set in the property
	 */
	public void setFieldOfViewDeg(double fov) {
		fieldOfViewDeg.set(fov);
	}

	/**
	 * Getter for the value of field of view property (in degrees)
	 * 
	 * @return value of field of view property (in degrees)
	 */
	public double getFieldOfViewDeg() {
		return fieldOfViewDeg.get();
	}

	/**
	 * Getter for the field of view property
	 * 
	 * @return field of view property
	 */
	public DoubleProperty fieldOfViewDegProperty() {
		return fieldOfViewDeg;
	}

	//CENTER COORDINATES PROPERTY

	/**
	 * Setter for the coordinates of the center
	 * 
	 * @param hor
	 * 			Coordinates to be set in
	 */
	public void setCenter(HorizontalCoordinates hor) {
		lonDeg.set(hor.azDeg());
		altDeg.set(hor.altDeg());
	}

	/**
	 * Getter for the value of lon deg property
	 * 
	 * @return value of lon deg property
	 */
	public double getCenterLonDeg() {return lonDeg.getValue();}

	/**
	 * Setter for lon deg property
	 * 
	 * @param longitude
	 * 			Value to be set in the property
	 * 
	 */
	public void setCenterLonDeg(double longitude) {lonDeg.set(longitude);}

	/**
	 * Getter for the value of alt deg property
	 * 
	 * @return value of alt deg property
	 */
	public double getCenterAltDeg() {return altDeg.getValue();}

	/**
	 * Setter for the alt deg property
	 * 
	 * @param alt
	 * 			Value to be set in the property
	 */
	public void setCenterAltDeg(double alt) {altDeg.set(alt);}

	/**
	 * Getter for the value of the center coordinates property
	 * 
	 * @return value of the center coordinates property
	 */
	public HorizontalCoordinates getCenterCoordinates() {
		return centerCoordinates.get();
	}

	/**
	 * Getter for the center coordinates property
	 * 
	 * @return center coordinates property
	 */
	public ObjectBinding<HorizontalCoordinates> centerCoordinatesProperty() {
		return centerCoordinates;
	}
}
