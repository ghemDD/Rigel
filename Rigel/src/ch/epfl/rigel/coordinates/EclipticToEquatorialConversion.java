package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import static ch.epfl.rigel.math.Angle.*; 

/**
 * Conversion from ecliptic coordinates to equatorial coordinates
 * @author Nael Ouerghemi
 *
 */
public class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{
	private double cosecl, sinecl;


	public EclipticToEquatorialConversion(ZonedDateTime when) {
		Polynomial p=Polynomial.of(ofDMS(0, 0, 0.00181), new double[] {ofDMS(0, 0, 0.0006), ofDMS(0, 0, 46.815),  ofDMS(23, 26, 21.45)});
		double julian=Epoch.J2000.julianCenturiesUntil(when);
		double obl=p.at(julian);
		cosecl=Math.cos(obl);
		sinecl=Math.sin(obl);
	}

	@Override
	public EquatorialCoordinates apply(EclipticCoordinates t) {
		// TODO Auto-generated method stub
		double exp=Math.sin(t.lon())*cosecl-Math.tan(t.lat())*sinecl;
		double ralpha=Math.atan2(exp, Math.cos(t.lon()));
		double alpha= ralpha >=0 ? ralpha : ralpha+TAU;

		double term1=Math.sin(t.lat())*cosecl;
		double term2=Math.cos(t.lat())*sinecl*Math.sin(t.lon());

		double beta=Math.asin(term1+term2);

		return EquatorialCoordinates.of(alpha, beta);
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
