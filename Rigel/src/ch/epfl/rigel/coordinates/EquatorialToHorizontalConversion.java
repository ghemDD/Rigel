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
	private double latobs, lonobs;


	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		horangle=local(when, where)-where.lon();
		latobs=where.lat();
		lonobs=where.lon();
	}

	//Temporary for testing purposes only
	public EquatorialToHorizontalConversion(double horangle, double latobs) {
		this.horangle=horangle;
		this.latobs=latobs;
	}
	
	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates t) {
		// TODO Auto-generated method stub

		System.out.println("Angle horaire : "+toDeg(horangle));
		System.out.println("Latitude : "+t.latDeg());
		
		double term1=Math.sin(t.lat())*Math.sin(latobs);
		double term2=Math.cos(t.lat())*Math.cos(latobs)*Math.cos(horangle);

		double h=Math.asin(term1+term2);
		System.out.println("a ="+toDeg(h));

		double den=Math.sin(t.lat())-Math.sin(latobs)*Math.sin(h);
		double num=-Math.cos(t.lat())*Math.cos(latobs)*Math.sin(horangle);
		
		double num1=den;
		double den1=Math.cos(latobs)*Math.cos(h);
		
		//System.out.println("cos A = "+num1/den1);
		//System.out.println("A = "+toDeg(Math.acos(num1/den1)));
		//System.out.println("sin H = "+Math.sin(horangle));
		
		System.out.println("X ="+den);
		System.out.println("Y ="+num);
		

		double raw=Math.atan2(num, den);
		System.out.println("Raw value :"+raw);
		System.out.println("Raw :"+toDeg(raw));
		double az= den>=0 ? (num >=0 ? raw : raw+TAU) : raw+Math.PI;
		//double az = Angle.normalizePositive(raw);
		//System.out.println("Raw AZ "+(az-TAU));
		
		
		System.out.println("Az : "+toDeg(az));

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
