package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import static ch.epfl.rigel.math.Angle.*;
import java.util.function.Function;
import static ch.epfl.rigel.astronomy.SiderealTime.*;

/**
 * Conversion from Equatorial Coordinates to Horizontal Coordinates depending on a certain location
 * (in Geographic Coordinates)
 * @author Nael Ouerghemi
 *
 */
public class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{

	private double hor;
	private double latobs, lonobs;


	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		hor=local(when, where)-where.lon();
		latobs=where.lat();
		lonobs=where.lon();
	}

	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates t) {
		// TODO Auto-generated method stub

		double term1=Math.sin(t.lat())*Math.sin(latobs);
		double term2=Math.cos(t.lat())*Math.cos(latobs)*Math.acos(hor);

		double h=Math.asin(term1+term2);

		double den=Math.sin(t.lat())-Math.sin(latobs)*Math.sin(h);
		double num=-Math.cos(t.lat())*Math.cos(latobs)*Math.sin(hor);

		double raw=Math.atan2(num, den);
		double az= raw >= 0 ? raw : (den >=0 ? raw+TAU : raw+Math.PI);

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
