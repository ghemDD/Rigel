package ch.epfl.rigel.math;

import ch.epfl.rigel.coordinates.CartesianCoordinates;

/**
 * Computes the euclidean distance between two vectors
 * 
 * @author Nael Ouerghemi (310435)
 */
public interface EuclideanDistance {

	/**
	 * Compute the euclidean distance between two vectors
	 * 
	 * @param vector1
	 * 			1st vector
	 * 
	 * @param vector2
	 * 			2nd vector
	 * 
	 * @return euclidean distance between the two vectors
	 */
	public static double distance(double[] v1, double[] v2) {
		double distance = 0;

		distance = (v1[0] - v2[0])*(v1[0] - v2[0]) + (v1[1] - v2[1])*(v1[1] - v2[1]);

		return Math.sqrt(distance);
	}

	/**
	 * Compute the Euclidean distance between two points given their cartesian coordinates
	 * 
	 * @param coor1
	 * 			1st point
	 * 
	 * @param coor2
	 * 			2nd point
	 * 
	 * @return euclidean distance between the two points
	 */
	public static double cartesianDistance(CartesianCoordinates coor1, CartesianCoordinates coor2) {
		double[] vector1 = new double[] {coor1.x(), coor1.y()};
		double[] vector2 = new double[] {coor2.x(), coor2.y()};
		return distance(vector1, vector2);
	}
}
