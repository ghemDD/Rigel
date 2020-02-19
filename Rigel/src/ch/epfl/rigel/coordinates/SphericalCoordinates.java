package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.*;

abstract class SphericalCoordinates {
	private double lon;
	private double lat;
	
	SphericalCoordinates(double longitude, double latitude) {
		lon=longitude;
		lat=latitude;
	}
	
	double lon() {return lon;}
	
	double lonDeg() {return toDeg(lon);}
	
	double lat() {return lat;}
	
	double latDeg() {return toDeg(lat);}
	
	@Override
	public final boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException();
	}
	
}
