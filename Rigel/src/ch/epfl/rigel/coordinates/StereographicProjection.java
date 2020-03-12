package ch.epfl.rigel.coordinates;

import java.util.Locale;
import static ch.epfl.rigel.math.Angle.*;
import java.util.function.Function;

import ch.epfl.rigel.math.Angle;

/**
 * Represents a stereographic projection of horizontal coordinates
 * @author Nael Ouerghemi
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

	private HorizontalCoordinates cent;
	private double cos;
	private double sin;
	
	public StereographicProjection(HorizontalCoordinates center) {
		cent=center;
		cos=Math.cos(cent.alt());
		sin=Math.sin(cent.alt());
	}

	@Override
	public CartesianCoordinates apply(HorizontalCoordinates t) {
		// TODO Auto-generated method stub
		
		double delta=t.az()-cent.az();
		double d=1/(1+sin*Math.sin(t.alt())+cos*Math.cos(t.alt())*Math.cos(delta));
		
		double x=d*Math.cos(t.alt())*Math.sin(delta);
		double y=d*(cos*Math.sin(t.alt())-sin*Math.cos(t.alt())*Math.cos(delta));
		
		return CartesianCoordinates.of(x, y);
	}
	
	/**
	 * 
	 * @param hor
	 * @return
	 */
	public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
		double den=sin+Math.sin(hor.alt());
		
		return CartesianCoordinates.of(0, cos/den);
	}
	
	/**
	 * 
	 * @param parallel
	 * @return
	 */
	public double circleRadiusForParallel(HorizontalCoordinates parallel) {
		double den=sin+Math.sin(parallel.alt());
		double p=Math.cos(parallel.alt())/den;
		
		return p;
	}
	
	/**
	 * 
	 * @param rad
	 * @return
	 */
	public double applyToAngle(double rad) {
		
		return 2*Math.tan(rad/4);
	}
	
	/**
	 * 
	 * @param xy
	 * @return
	 */
	public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
		double p=Math.sqrt(xy.x()*xy.x()+xy.y()*xy.y());
		double sinc=2*p/(p*p+1);
		double cosc=(1-p*p)/(p*p+1);
		
		double numdelta=xy.x()*sinc;
		double dendelta=p*cos*cosc-xy.y()*sin*sinc;
		double delta=Math.atan2(numdelta, dendelta)+cent.az();
		delta=normalizePositive(delta);
		
		double term1=cosc*sin;
		double term2=(xy.y()*sinc*cos)/p;
		double phi=Math.asin(term1+term2);
		
		System.out.println("Potential az = "+delta+" Potential alt= "+phi);
		return HorizontalCoordinates.of(delta, phi);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "StereographicProjection Center coordinates "+cent.toString());
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
