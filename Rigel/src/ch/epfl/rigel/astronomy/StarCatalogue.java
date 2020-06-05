package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a star catalogue
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class StarCatalogue {
	private List<Star> stars;
	private Map<Asterism, List<Integer>> map;
	private List<String> starNames;

	/**
	 * StarCatalogue constructor
	 *  
	 * @param stars
	 * 			List of stars 
	 * 
	 * @param asterisms
	 * 			List of asterisms
	 * 
	 * @throws IllegalArgumentException
	 * 			if an asterism contains a star that is not in the list of stars
	 */
	public StarCatalogue(List<Star> stars, List<Asterism> asterisms) throws IllegalArgumentException{

		map = new HashMap<Asterism, List<Integer>>();
		this.stars = List.copyOf(stars);
		starNames = new ArrayList<String>();

		//Initialization of the map linking a Star and its index in the List<Star> stars
		//Faster access to the Star given the index than a linear search, (in order to create the map linking Asterisms and the list of indices of its stars) 
		Map<Star, Integer> indexMap = new HashMap<Star, Integer>();
		int i = 0;
		for(Star star : stars) {
			indexMap.put(star, i);

			if (star.name().charAt(0) != '?')
				starNames.add(star.name());

			++i;
		}


		for(Asterism ast : asterisms) {
			List<Star> listars = ast.stars();
			List<Integer> indexAst = new ArrayList<Integer>();

			for(Star cur : listars) {
				Integer index = indexMap.get(cur);

				if (index == null)
					throw new IllegalArgumentException();

				else {
					indexAst.add(index);
				}
			}

			map.put(ast, indexAst);
		}
	}

	/**
	 * Returns an immutable copy of the attribute List<Star> stars (copy made in constructor)
	 * 
	 * @return Immutable copy of stars
	 */
	public List<Star> stars() {
		return stars;
	}

	/**
	 * Returns the names of named stars (that do not start with '?' character)
	 * 
	 * @return names of named stars (that do not start with '?' character)
	 */
	public List<String> getStarNames() {
		return starNames;
	}

	/**
	 * Returns immutable copy of the set of asterisms
	 * 
	 * @return Set of asterisms
	 */
	public Set<Asterism> asterisms() {
		return Set.copyOf(map.keySet());
	}

	/**
	 * Returns the list of indices associated to the stars of the asterism
	 * 
	 * @param asterism 
	 * 			asterism given
	 * 
	 * @throws IllegalArgumentException 
	 * 			if asterism given is not in the catalogue
	 * 
	 * @return list of star indices related to the asterism
	 * 
	 */
	public List<Integer> asterismIndices(Asterism asterism) throws IllegalArgumentException {
		if (!asterisms().contains(asterism))
			throw new IllegalArgumentException();

		return List.copyOf(map.get(asterism));
	}

	/**
	 * StarCatalogue builder
	 * 
	 * @author Nael Ouerghemi (310435)
	 *
	 */
	public static final class Builder {
		private List<Star> stars;
		private List<Asterism> asterisms;
		private Map<Integer, Star> map;

		/**
		 * Constructor of the builder 
		 * Initialization of the lists/map
		 */
		public Builder() {
			stars = new ArrayList<Star>();
			asterisms = new ArrayList<Asterism>();
			//Initialization of the map linking the hipparcosId of the star and the star itself
			//Faster access to the Star given the hipparcosId than a linear search
			//It assumes that the list of stars is loaded before the list of asterisms
			map = new HashMap<Integer, Star>();
		}

		/**
		 * Adds an star to the attribute List<Asterism> stars
		 * 
		 * @param star 
		 * 			Star to be added to the attribute List<Asterism> stars
		 * 
		 * @return Current builder with the updated attribute
		 */
		public Builder addStar(Star star) {
			stars.add(star);
			map.put(star.hipparcosId(), star);

			return this;
		}

		/**
		 * Adds an asterism to the attribute List<Asterism> asterisms
		 * 
		 * @param asterism
		 * 			Asterism to be added to the attribute List<Asterism> asterisms
		 * 
		 * @return Current builder with the update attribute
		 */
		public Builder addAsterism(Asterism asterism) {
			asterisms.add(asterism);

			return this;
		}

		/**
		 * Give an unmodifiable view on the attribute List<Asterism> stars
		 *
		 * @return Unmodifiable view of stars
		 */
		public List<Star> stars() {
			return Collections.unmodifiableList(stars);
		}

		/**
		 * Give an unmodifiable view on the attribute List<Asterism> asterisms
		 * 
		 * @return Unmodifiable view of asterisms
		 */
		public List<Asterism> asterisms() {
			return Collections.unmodifiableList(asterisms);
		}

		/**
		 * Returns an immutable copy of mapping between Stars and their hipparcosId
		 * 
		 * @return immutable copy of mapping between Stars and their hipparcosId
		 */
		public Map<Integer, Star> map() {
			return Map.copyOf(map);
		}

		/**
		 * Loads the lists attributes of the Builder given
		 *  
		 * @param inputStream
		 * 			Resource file used input stream
		 * 		
		 * @param loader 
		 * 			Type of loader
		 * 
		 * @throws IOException
		 * 			if inputStream throws IOException 
		 * 
		 * @return CurrentBuilder with the updated attributes
		 *  
		 */
		public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
			loader.load(inputStream, this);

			return this;
		}

		/**
		 * Build Star Catalogue given the previous attributes List<Star> stars and List<Asterism> asterisms assigned to the builder
		 * 
		 * @return StarCatalogue with the given attributes of the builder
		 */
		public StarCatalogue build() {
			return new StarCatalogue(stars, asterisms);
		}

	}

	/**
	 * Represents  loader of catalogue of stars and asterisms
	 * 
	 * @author Nael Ouerghemi (310435)
	 *
	 */
	public interface Loader {

		public abstract void load(InputStream inputStream, Builder builder) throws IOException ;
	}
}
