package ch.epfl.rigel.math;
import static ch.epfl.rigel.Preconditions.checkArgument;

public final class Angle {
	//ChangesMOMO
	public final static double TAU=2*Math.PI;
	private static final double DEG_PER_RAD = 360.0 / TAU;
	private static final double RAD_PER_DEG= TAU/360.0;
	
	 
	
	public final static double RAD_PER_HR=Math.toRadians(15.0);
	public final static double RAD_PER_MIN=Math.toRadians(15.0/60.0);
	public final static double RAD_PER_SEC=Math.toRadians(15.0/3600.0);
	
	
	public static double normalizePositive(double rad) {
		RightOpenInterval normal=RightOpenInterval.of(0,  TAU);
		return normal.reduce(rad);
	}
	
	public static double ofArcsec(double sec) {
		return (sec*RAD_PER_SEC);
	}
	
	public static double ofDMS(int deg, int min, double sec) {
		checkArgument((0<=min && min<60) && (0<=sec && sec<60));
		
		return ofDeg(deg)+min*RAD_PER_MIN+sec*RAD_PER_SEC;
	}
	
	public static double ofDeg(double deg) {
		  return deg * RAD_PER_DEG;
		}
	
	public static double toDeg(double rad) {
		  return rad * DEG_PER_RAD;
		}
	
	public static double ofHr(double hr) {
		return (hr * RAD_PER_HR);
	}
	
	public static double toHr(double rad) {
		return rad / RAD_PER_HR;
	}
}
