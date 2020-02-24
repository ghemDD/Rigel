package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;
import static ch.epfl.rigel.astronomy.SiderealTime.*;

public class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{
	
	private double hor;
	private double latobs;
	
	
	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		hor=local(when, where)-where.lon();
		latobs=where.lat();
	}
	
	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates t) {
		// TODO Auto-generated method stub
		double num=Math.sin(t.lat())-Math.sin(latobs)*Math.sin(t.lon());
		double den=Math.cos(t.lon())*Math.cos(latobs);
		
		double az=Math.acos(num/den);
		
		double term1=Math.sin(t.lat())*Math.sin(latobs);
		double term2=Math.cos(t.lat())*Math.cos(latobs)*Math.acos(hor);
		
		
		double h=Math.asin(term1+term2);
		
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
