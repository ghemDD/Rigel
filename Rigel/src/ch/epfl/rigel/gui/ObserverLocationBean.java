package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Bean containing the observer's location
 * 
 * @author Nael Ouerghemi (310435)
 */
public class ObserverLocationBean {

	private final DoubleProperty lonDeg;
	private final DoubleProperty latDeg;
	private final ObjectBinding<GeographicCoordinates> geographicCoordinates;

	/**
	 * Constructor for the observerLocationBean : instantiates the properties/Binding
	 */
	public ObserverLocationBean() {
		lonDeg = new SimpleDoubleProperty();
		latDeg = new SimpleDoubleProperty();
		geographicCoordinates = Bindings.createObjectBinding(
				() -> ( GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get())), 
				lonDeg, latDeg);
	}

	// LON DEG PROPERTY
	/**
	 * Setter for lonDeg
	 * 
	 * @param value
	 * 			Longitude of the observer's location (in degrees)
	 * 
	 */
	public void setLonDeg(double value) {		
		lonDeg.set(value);
	}

	/**
	 * Getter for the value of lonDeg
	 * 
	 * @return Longitude of the observer's location (in degrees)
	 */
	public double getLonDeg() {
		return lonDeg.get();
	}

	/**
	 * Getter for the property lonDeg
	 * 
	 * @return property lonDeg
	 */
	public DoubleProperty lonDegProperty() {
		return lonDeg;
	}

	// LAT DEG PROPERTY
	/**
	 * Setter for latDeg
	 * 
	 * @param value
	 * 			Latitude of the observer's location (in degrees)
	 * 
	 */
	public void setLatDeg(double value) {
		latDeg.set(value);
	}

	/**
	 * Getter for the value of latDeg
	 * 
	 * @return Latitude of the observer's location (in degrees)
	 */
	public double getLatDeg() {
		return latDeg.get();
	}

	/**
	 * Getter for the property latDeg
	 * 
	 * @return property latDeg
	 */
	public DoubleProperty latDegProperty() {
		return latDeg;
	}

	// GEOGRAPHIC COORDINATES BINDING
	/**
	 * Getter for the value of geographicCoordinates
	 * 
	 * @return Geographic Coordinates of the observer's location
	 */
	public GeographicCoordinates getGeographicCoordinates() {
		return geographicCoordinates.get();
	}

	/**
	 * Setter for GeographicCoordinatesProperty
	 * 
	 * @param geo
	 * 			Geographic coordinates to set in the property
	 */
	public void setCoordinates(GeographicCoordinates geo) {
		lonDeg.set(geo.lonDeg());
		latDeg.set(geo.latDeg());
	}

	/**
	 * Getter for the geographicCoordinatesBinding
	 * 
	 * @return Binding geographicCoordinatesBinding
	 */
	public ObjectBinding<GeographicCoordinates> getGeographicCoordinatesBinding() {
		return geographicCoordinates;
	}
}
