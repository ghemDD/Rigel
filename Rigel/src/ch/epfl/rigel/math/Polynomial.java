package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;

/**
 * A polynomial
 * @author Nael Ouerghemi
 *
 */
public final class Polynomial {
	private double[] coefficients;

	private Polynomial (double coefficientN, double[] coefficientsP) {
		coefficients=new double[1+coefficientsP.length];
		coefficients[0]=coefficientN;
		double[] temp=new double[coefficientsP.length];
		System.arraycopy(coefficientsP, 0, temp, 0, temp.length);
		System.arraycopy(temp, 0, coefficients, 1, temp.length);
	}

	/**
	 * Create a polynomial with respect to the parameters given below
	 * @param coefficientN : value of the first coefficient (can't be null)
	 * @param coefficients : array of the next coefficients
	 * @return desired Polynomial
	 */
	public static Polynomial of(double coefficientN, double... coefficients) {
		checkArgument(coefficientN!=0);
		return new Polynomial(coefficientN, coefficients);
	}

	/**
	 * Override of the function toString of Object
	 * Return the string representing the polynomial for example : 5x^2+8x+5
	 * 		Common representation : 5x^1 is incorrect but 5x is correct
	 * 								5x^0 is incorrect but 5 is correct
	 */
	@Override
	public String toString() {
		int count=coefficients.length-1;
		StringBuilder string=new StringBuilder();
		boolean first=true;

		for(int i=0; i<coefficients.length; ++i) {
			if (coefficients[i]!=0) {
				/**
				 * Checks if a sign has to be added : if the coefficient is the first term of the polynomial
				 * boolean first is only true for the first coefficient namely coefficients[0]
				 */

				if (!first) {
					if (coefficients[i]>=0) 
						string.append("+");
				}

				/**
				 * Adds the sign minus whether or not it is the first term
				 */
				if (coefficients[i]<0)
					string.append("-");

				/**
				 * Checks if the coefficients is different from 1 in which case its absolute value is added to the StringBuilder
				 */
				if (Math.abs(coefficients[i])!=1.0)
					string.append(Math.abs(coefficients[i]));

				/**
				 * Int count represents the degree of the variable x
				 * Necessity to check if the degree is different from 0 (to not output x^n+...+8x^0 for example)
				 * Also if it is different from one (to not output 5x^1+4 instead of 5x+4)
				 */
				if (count!=0) {
					string.append("x");

					if (count!=1)
						string.append("^"+count);
				}
			}
			--count;
			first=false;
		}
		return string.toString();	
	}

	/**
	 * Calculate the polynomial using Horner's method
	 * @param v : value for which the polynomial is calculated
	 * @return the value of the polynomial with respect to v
	 */
	public double at(double v) {

		double sum=coefficients[0];

		for(int i=1; i<coefficients.length; ++i) {
			sum= sum * v + coefficients[i];
		}

		return sum;
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
