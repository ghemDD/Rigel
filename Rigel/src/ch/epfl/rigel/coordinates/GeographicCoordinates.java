package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import static ch.epfl.rigel.Preconditions.*;

public final class GeographicCoordinates extends SphericalCoordinates {
	
	
	private GeographicCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}
	
	public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
		isValidLonDeg(lonDeg);
		isValidLatDeg(latDeg);
		
		return new GeographicCoordinates(lonDeg, latDeg);
	}

	public static boolean isValidLonDeg(double lonDeg) {
		RightOpenInterval longitudeInt=RightOpenInterval.symmetric(360);
		checkInInterval(longitudeInt, lonDeg);
		
		return true;
	}
	
	public static boolean isValidLatDeg(double latDeg) {
		ClosedInterval latInt=ClosedInterval.symmetric(180);
		checkInInterval(latInt, latDeg);
		
		return true;
	}
	
	@Override
	public double lon() {return super.lon();}
	
	@Override
	public double lonDeg() {return super.lonDeg();}
	
	@Override
	public double lat() {return super.lat();}
	
	@Override
	public double latDeg() {return super.latDeg();}
	
	@Override 
	public String toString() {
		return String.format("(lon=%s°, lat=%s°)", lonDeg(), latDeg());
	}
}
