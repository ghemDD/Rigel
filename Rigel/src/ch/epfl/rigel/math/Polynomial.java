package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;

public final class Polynomial {
	private double[] coefficients;
	
	private Polynomial (double coefficientN, double[] coefficientsP) {
		coefficients=new double[1+coefficientsP.length];
		coefficients[0]=coefficientN;
		double[] temp=new double[coefficientsP.length];
		System.arraycopy(coefficientsP, 0, temp, 0, temp.length);
		System.arraycopy(temp, 0, coefficients, 1, temp.length);
	}
	
	public static Polynomial of(double coefficientN, double... coefficients) {
		checkArgument(coefficientN!=0);
		return new Polynomial(coefficientN, coefficients);
	}
	
	public String toString() {
		int count=coefficients.length-1;
		StringBuilder string=new StringBuilder();
		
		for(int i=0; i<coefficients.length; ++i) {
			if (coefficients[i]!=0) {
				string.append(coefficients[i]);
				if (count!=0) {
					string.append("x^"+count);
					string.append("+");
				}
					
				else
					string.append(count);
				
				
			}
			--count;
		}
	
		return string.toString();	
	}
	
	public double at(double v) {
		int count=coefficients.length-1;
		double poly=0.0;
		double pow=1.0;
		
		for(int i=0; i<coefficients.length; ++i) {
			pow=1.0;
			for(int y=0; y<count; ++y) {
				pow*=v;
			}
			poly+=pow*coefficients[i];
			--count;
		}
		return poly;
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
