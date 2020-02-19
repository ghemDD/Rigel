package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.rigel.math.Angle;

public final class HorizontalCoordinates extends SphericalCoordinates {

	private HorizontalCoordinates(double longitude, double latitude) {
		super(longitude, latitude);
	}
	
	public static HorizontalCoordinates of(double az, double alt) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, Angle.TAU);
		checkInInterval(azIntDeg, az);
		
		ClosedInterval altInt=ClosedInterval.symmetric(Math.PI);
		checkInInterval(altInt, alt);
		
		return new HorizontalCoordinates(az , alt);
	}

	
	public static HorizontalCoordinates ofDeg(double az, double alt) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, az);
		
		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, alt);
		
		
		return new HorizontalCoordinates(az, alt);
	}
	
	public double az() {return super.lon();}
	
	public double azDeg() {return super.lonDeg();}
	
	public double alt() {return super.lat();}
	
	public double altDeg() {return super.latDeg();}
	
	/*
	public String azOctantName(String n, String e, String s, String w) {
		
	}
	*/
	
	@Override
	public String toString() {
		return String.format("(az=%s°, alt=%s°)", lonDeg(), latDeg());
	}
}
