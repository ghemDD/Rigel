package ch.epfl.rigel.astronomy;

import java.util.List;
import static ch.epfl.rigel.Preconditions.*;

/**
 * Represents an asterism
 * @author Nael Ouerghemi
 *
 */
public final class Asterism {

	private List<Star> stars;

	//Public or package private 
	Asterism(List<Star> stars) {
		checkArgument(!stars.isEmpty());

		this.stars=List.copyOf(stars);
	}

	public List<Star> stars() {return List.copyOf(stars);}
}
