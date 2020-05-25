package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.astronomy.SiderealTime.local;

import static ch.epfl.rigel.math.Angle.normalizePositive;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion from Equatorial Coordinates to Horizontal Coordinates depending on a certain location
 * (in Geographic Coordinates)
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates>{
	private final double sinLatObs, cosLatObs;
	private final double time;

	/**
	 * Constructs an EquatorialToHorizontalConversion given a ZonedDateTime and a location in geographic coordinates
	 * 
	 * @param when
	 * 			Zoned date time
	 * 
	 * @param where
	 * 			Geographic coordinates of the point of observation
	 */
	public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
		time = local(when, where);
		double latObs = where.lat();
		sinLatObs = sin(latObs);
		cosLatObs = cos(latObs);
	}

	/**
	 * Returns the conversion from equatorial coordinates to horizontal coordinates
	 * 
	 * @param t
	 * 			Equatorial coordinates
	 * 
	 * @return conversion from equatorial coordinates to horizontal coordinates
	 */
	@Override
	public HorizontalCoordinates apply(EquatorialCoordinates t) {

		//Repeated sin/cos term
		double sinCoor = sin(t.dec());
		double cosCoor = cos(t.dec());

		//Hour angle
		double horangle = time - t.ra();

		//Latitude
		double termA = sinCoor * sinLatObs;
		double termB = cosCoor * cosLatObs * cos(horangle);
		double latTerm = termA + termB;

		//Azimuth
		double den = sinCoor - sinLatObs*latTerm;
		double num = -cosCoor * cosLatObs * sin(horangle);
		double az = normalizePositive(atan2(num, den));
		double lat = asin(latTerm);

		return HorizontalCoordinates.of(az, lat);
	}

	/**
	 * @see Object#equals()
	 */
	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}
