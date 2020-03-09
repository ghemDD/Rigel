package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import static ch.epfl.rigel.math.Angle.*;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

import static ch.epfl.rigel.astronomy.SiderealTime.*;

/**
 * Conversion from Equatorial Coordinates to Horizontal Coordinates depending on a certain location
 * (in Geographic Coordinates)
 * @author Nael Ouerghemi
 *
 */
public class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{
	private double horangle;
	private double latobs;
	private double time;


	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		time=local(when, where);
		latobs=where.lat();
	}

	//Temporary for testing purposes only
	public EquatorialToHorizontalConversion(double horangle, double latobs) {
		this.horangle=horangle;
		this.latobs=latobs;
	}
	
	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates t) {
		// TODO Auto-generated method stub
		//Constantes publiques 
		horangle=time-t.ra();
		double term1=Math.sin(t.dec())*Math.sin(latobs);
		double term2=Math.cos(t.dec())*Math.cos(latobs)*Math.cos(horangle);

		double h=Math.asin(term1+term2);
		
		double den=Math.sin(t.dec())-Math.sin(latobs)*Math.sin(h);
		double num=-Math.cos(t.dec())*Math.cos(latobs)*Math.sin(horangle);

		double raw=Math.atan2(num, den);
		double az = Angle.normalizePositive(raw);
		
		
		return HorizontalCoordinates.of(az, h);
	}
	
	
	public HorizontalCoordinates applyTest(EquatorialCoordinates t) {
		// TODO Auto-generated method stub
		//Constantes publiques 
		double term1=Math.sin(t.dec())*Math.sin(latobs);
		double term2=Math.cos(t.dec())*Math.cos(latobs)*Math.cos(horangle);

		double h=Math.asin(term1+term2);
		
		double den=Math.sin(t.dec())-Math.sin(latobs)*Math.sin(h);
		double num=-Math.cos(t.dec())*Math.cos(latobs)*Math.sin(horangle);

		double raw=Math.atan2(num, den);
		
		double az = Angle.normalizePositive(raw);
		
		return HorizontalCoordinates.of(az, h);
	}


	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

}
