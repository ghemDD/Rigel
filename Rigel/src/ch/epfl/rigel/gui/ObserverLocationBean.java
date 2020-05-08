package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;

public class ObserverLocationBean {

	private ObjectProperty<Double> lonDegProperty;
	private ObjectProperty<Double> latDegProperty;
	private ObjectBinding<GeographicCoordinates> geographicCoordinatesProperty;


	public ObserverLocationBean() {
		lonDegProperty = new SimpleObjectProperty<Double>();
		latDegProperty = new SimpleObjectProperty<Double>();
		geographicCoordinatesProperty = Bindings.createObjectBinding(() -> ( GeographicCoordinates.ofDeg(lonDegProperty.get(), latDegProperty.get())), lonDegProperty, latDegProperty);
	}

	// Lon Deg Property
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
	public ObjectProperty<Double> getLonDegProperty() {
		return lonDegProperty;
	}

	// Lat Deg Property
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
	public ObjectProperty<Double> getLatDegProperty() {
		return latDegProperty;
	}

	// Geographic Coordinates Property
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
