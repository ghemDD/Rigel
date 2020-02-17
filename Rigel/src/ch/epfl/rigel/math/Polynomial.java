package ch.epfl.rigel.math;

public final class Polynomial {
	private double[] coefficients;
	
	private Polynomial (double coefficientN, double[] coefficientsP) {
		coefficients=new double[1+coefficientsP.length];
		coefficients[0]=coefficientN;
		double[] temp=new double[coefficientsP.length];
		System.arraycopy(coefficientsP, 0, temp, 0, temp.length);
		System.arraycopy(temp, 0, coefficients, 1, temp.length);
	}
	
	public static Polynomial of(double coefficientN, double[] coefficients) {
		return new Polynomial(coefficientN, coefficients);
	}
	
	public String toString() {
		int count=coefficients.length;
		StringBuilder string=new StringBuilder();
		
		for(int i=0; i<coefficients.length; ++i) {
			if (coefficients[i]!=0) {
				string.append(coefficients[i]+" ");
				if (count!=0)
					string.append("x^"+count);
				
				else
					string.append(count);
			}
			--count;
		}
	
		return string.toString();	
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
