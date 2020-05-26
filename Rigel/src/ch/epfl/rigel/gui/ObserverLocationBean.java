package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Bean containing the observer's location
 * 
 * @author Nael Ouerghemi (310435)
 */
public class ObserverLocationBean {

	private final DoubleProperty lonDegProperty;
	private final DoubleProperty latDegProperty;
	private final ObjectBinding<GeographicCoordinates> geographicCoordinatesProperty;


	public ObserverLocationBean() {
		lonDegProperty = new SimpleDoubleProperty();
		latDegProperty = new SimpleDoubleProperty();
		geographicCoordinatesProperty = Bindings.createObjectBinding(() -> ( GeographicCoordinates.ofDeg(lonDegProperty.get(), latDegProperty.get())), lonDegProperty, latDegProperty);
	}

	// LON DEG PROPERTY
	/**
	 * Setter for lonDegProperty
	 * 
	 * @param value
	 * 			Longitude of the observer's location (in degrees)
	 * 
	 */
	public void setLonDeg(double value) {		
		lonDegProperty.set(value);
	}

	/**
	 * Getter for the value of lonDegProperty
	 * 
	 * @return Longitude of the observer's location (in degrees)
	 */
	public double getLonDeg() {
		return lonDegProperty.get();
	}

	/**
	 * Getter for the property lonDegProperty
	 * 
	 * @return property lonDegProperty
	 */
	public DoubleProperty getLonDegProperty() {
		return lonDegProperty;
	}

	// LAT DEG PROPERTY
	/**
	 * Setter for latDegProperty
	 * 
	 * @param value
	 * 			Latitude of the observer's location (in degrees)
	 * 
	 */
	public void setLatDeg(double value) {
		latDegProperty.set(value);
	}

	/**
	 * Getter for the value of latDegProperty
	 * 
	 * @return Latitude of the observer's location (in degrees)
	 */
	public double getLatDeg() {
		return latDegProperty.get();
	}

	/**
	 * Getter for the property latDegProperty
	 * 
	 * @return property latDegProperty
	 */
	public DoubleProperty getLatDegProperty() {
		return latDegProperty;
	}

	// GEOGRAPHIC COORDINATES BINDING
	/**
	 * Getter for the value of geographicCoordinatesProperty
	 * 
	 * @return Geographic Coordinates of the observer's location
	 */
	public GeographicCoordinates getGeographicCoordinates() {
		return geographicCoordinatesProperty.get();
	}

	/**
	 * Setter for GeographicCoordinatesProperty
	 * 
	 * @param geo
	 * 			Geographic coordinates to set in the property
	 * 
	 */
	public void setCoordinates(GeographicCoordinates geo) {
		lonDegProperty.set(geo.lonDeg());
		latDegProperty.set(geo.latDeg());
	}

	/**
	 * Getter for the geographicCoordinatesBinding
	 * 
	 * @return Binding geographicCoordinatesBinding
	 */
	public ObjectBinding<GeographicCoordinates> getGeographicCoordinatesBinding() {
		return geographicCoordinatesProperty;
	}
}
