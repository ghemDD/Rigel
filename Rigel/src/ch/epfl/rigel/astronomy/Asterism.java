package ch.epfl.rigel.astronomy;

import java.util.List;
import static ch.epfl.rigel.Preconditions.*;

/**
 * Represents an asterism
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class Asterism {

	private List<Star> stars;

	/**
	 * Constructor for Asterism 
	 * 
	 * @param stars 
	 * 			list of stars of the asterism
	 * 
	 * @throws IllegalArgumentException 
	 * 			if List<Star> stars is empty
	 */
	public Asterism(List<Star> stars) {
		checkArgument(!stars.isEmpty());

		this.stars = List.copyOf(stars);
	}

	/**
	 * Getter for the list of stars of the asterism
	 * 
	 * @return list of the stars of the asterism
	 */
	public List<Star> stars() {return stars;}
}
