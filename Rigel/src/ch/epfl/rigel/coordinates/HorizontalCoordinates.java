package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.Preconditions.checkInInterval;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.rigel.math.Angle;

/**
 * Horizontal Coordinates
 * @author Nael Ouerghemi / Tanguy Marbot
 *
 */
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

	
	/**
	 * 
	 * @param az
	 * @param alt
	 * @return
	 */
	public static HorizontalCoordinates ofDeg(double az, double alt) {
		RightOpenInterval azIntDeg=RightOpenInterval.of(0, 360);
		checkInInterval(azIntDeg, az);
		
		ClosedInterval altInt=ClosedInterval.symmetric(180);
		checkInInterval(altInt, alt);
		
		
		return new HorizontalCoordinates(az, alt);
	}
	
	/**
	 * 
	 * @return
	 */
	public double az() {return super.lon();}
	
	/**
	 * 
	 * @return
	 */
	public double azDeg() {return super.lonDeg();}
	
	/**
	 * 
	 * @return
	 */
	public double alt() {return super.lat();}
	
	/**
	 * 
	 * @return
	 */
	public double altDeg() {return super.latDeg();}
	
	
	/**
	 * 
	 * @param n
	 * @param e
	 * @param s
	 * @param w
	 * @return
	 */
	public String azOctantName(String n, String e, String s, String w) {
		StringBuilder string=new StringBuilder();
		
		RightOpenInterval north1=RightOpenInterval.of(315, 360);
		RightOpenInterval north2=RightOpenInterval.of(0, 45);
		
		ClosedInterval south=ClosedInterval.of(135, 225);
		
		ClosedInterval west=ClosedInterval.of(135, 225);
		
		ClosedInterval east=ClosedInterval.of(135, 225);
		
		if (north1.contains(az()) || north2.contains(az()))
			string.append(n);
		
		if (south.contains(az()))
			string.append(s);
		
		if (east.contains(az()))
			string.append(e);
		
		if (west.contains(az()))
			string.append(w);
		
		return string.toString();
	}
	
	/**
	 * 
	 * @param that
	 * @return
	 */
	public double angularDistanceTo(HorizontalCoordinates that) {
		double sinD=Math.sin(this.alt())*Math.sin(that.alt());
		double cosD=Math.cos(this.alt())*Math.acos(that.alt());
		double delta=Math.acos(this.az()-that.az());
		
		return Math.acos(sinD+cosD*delta);
	}
	
	
	@Override
	public String toString() {
		return String.format("(az=%s°, alt=%s°)", lonDeg(), latDeg());
	}
}
