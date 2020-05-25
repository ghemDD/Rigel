package ch.epfl.rigel.coordinates;

import static ch.epfl.rigel.math.Angle.normalizePositive;
import static ch.epfl.rigel.math.Angle.ofArcsec;
import static ch.epfl.rigel.math.Angle.ofDMS;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Polynomial; 

/**
 * Conversion from ecliptic coordinates to equatorial coordinates
 * 
 * @author Nael Ouerghemi (310435)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{
	private final double cosEcl, sinEcl;
	private final static Polynomial POLY = Polynomial.of(ofArcsec(0.00181), ofArcsec(-0.0006), ofArcsec(-46.815),  ofDMS(23, 26, 21.45)); 

	/**
	 * Constructor for EclipticToEquatorialConversion given a ZonedDate
	 * 
	 * @param when 
	 * 			Date of the conversion 
	 */
	public EclipticToEquatorialConversion(ZonedDateTime when) { 
		double julian = Epoch.J2000.julianCenturiesUntil(when);
		double obl = POLY.at(julian);
		cosEcl = cos(obl);
		sinEcl = sin(obl);
	}

	/**
	 * Returns the conversion from ecliptic coordinates to equatorial coordinates
	 * 
	 * @param t
	 * 			Equatorial coordinates
	 * 
	 * @return conversion from ecliptic coordinates to equatorial coordinates
	 */
	@Override
	public EquatorialCoordinates apply(EclipticCoordinates t) {
		//Repeated sin term
		double sinLonCoor = sin(t.lon());

		double exp = sinLonCoor*cosEcl - tan(t.lat())*sinEcl;
		double lon = normalizePositive( atan2(exp, cos(t.lon())) );

		double termA = sin(t.lat()) * cosEcl;
		double termB = cos(t.lat()) * sinEcl * sinLonCoor;

		double lat = asin(termA + termB);

		return EquatorialCoordinates.of(lon, lat);
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
