package ch.epfl.rigel.coordinates;

import java.time.ZonedDateTime;
import java.util.function.Function;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Polynomial;
import static ch.epfl.rigel.math.Angle.*; 

public class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates>{
	private double obl;
	
	
	
	public EclipticToEquatorialConversion(ZonedDateTime when) {
		Polynomial p=Polynomial.of(ofDMS(0, 0, 0.00181), new double[] {ofDMS(0, 0, 0.0006), ofDMS(0, 0, 46.815),  ofDMS(23, 26, 21.45)});
		double julian=Epoch.J2000.julianCenturiesUntil(when);
		obl=p.at(julian);
	}
	
	@Override
	public EquatorialCoordinates apply(EclipticCoordinates t) {
		// TODO Auto-generated method stub
		double num=Math.sin(t.lon())*Math.cos(obl)-Math.tan(t.lat())*Math.asin(obl);
		double alpha=Math.atan2(num/Math.cos(t.lon()), 1);
		
		double term1=Math.sin(t.lat())*Math.cos(obl);
		double term2=Math.acos(t.lat())*Math.asin(obl)*Math.sin(t.lon());
		
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
