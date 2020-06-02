package ch.epfl.rigel.astronomy;


import static ch.epfl.rigel.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;

/**
 * Represents the set of all celestial objects visible in the sky
 * 
 * @author Nael Ouerghemi (310435)
 * @author Tanguy Marbot (316756)
 */
public class ObservedSky {

	private final Map<CelestialObject, CartesianCoordinates> celestialCoordinates;
	private final List<Planet> planets;
	private final List<CartesianCoordinates> planetCartesianPositions;
	private final double[] planetPositions;

	private final StereographicProjection projection;
	private final EclipticToEquatorialConversion eclToEqu;
	private final EquatorialToHorizontalConversion equToHor;

	private final Sun sun;
	private final CartesianCoordinates sunPosition;

	private final Moon moon;
	private final CartesianCoordinates moonPosition;

	private final StarCatalogue catalogue;
	private final List<CartesianCoordinates> starCartesianPositions;
	private final double[] starPositions;
	
	//PATH
	private String starString;
	private CartesianCoordinates selectedStarCoordinates;

	/**
	 * Constructor for the ObservedSky class containing representing the set of all celestial objects visible in the sky
	 * 
	 * @param date 
	 * 			ZonedDateTime containing the necessary informations about time and date
	 * 
	 * @param observationPosition 
	 * 			GeographicCoordinates of the observer
	 * 
	 * @param stereo 
	 * 			StereographicProjection used to project in 2D plan the positions of the celestial objects
	 * 
	 * @param catalogue 
	 * 			StarCatalogue containing the list of stars and asterisms
	 * 
	 */
	public ObservedSky(ZonedDateTime date, GeographicCoordinates observationPosition, StereographicProjection stereo, StarCatalogue catalogue, String starString) {

		double daysSinceJ2010 = Epoch.J2010.daysUntil(date);
		eclToEqu = new EclipticToEquatorialConversion(date);
		equToHor = new EquatorialToHorizontalConversion(date, observationPosition);
		projection = stereo;
		planets = new ArrayList<Planet>();
		planetCartesianPositions = new ArrayList<>();
		starCartesianPositions = new ArrayList<>();
		this.catalogue = catalogue;
		this.starString = starString;
		celestialCoordinates = new HashMap<CelestialObject, CartesianCoordinates>();
		
		//pathCoordinates = new ArrayList<CartesianCoordinates>();

		sun = SunModel.SUN.at(daysSinceJ2010, eclToEqu);
		sunPosition = projectSingleCelestialObject(sun);

		moon = MoonModel.MOON.at(daysSinceJ2010, eclToEqu);
		moonPosition = projectSingleCelestialObject(moon);

		List<CelestialObject> planetsCelestial = constructPlanetList(daysSinceJ2010);
		planetPositions = projectListCelestialObject(planetsCelestial, planetCartesianPositions);

		List<CelestialObject> starsCelestial = starsCelestialObjects(catalogue);
		starPositions = projectListCelestialObject(starsCelestial, starCartesianPositions);
	}

	/**
	 * Projects single celestial objects (Moon and Sun)
	 * 
	 * @param object
	 * 			Single celestial object (Moon/Sun)
	 * 
	 * @return cartesian coordinates of the projection
	 */
	public CartesianCoordinates projectSingleCelestialObject(CelestialObject object) {
		HorizontalCoordinates horConvertedCoordinates = equToHor.apply(object.equatorialPos());
		CartesianCoordinates projCartCoordinates = projection.apply(horConvertedCoordinates);

		celestialCoordinates.put(object, projCartCoordinates);

		return projCartCoordinates;
	}

	/**
	 * Projects a list of celestial objects depending on the stereographic projection attribute and conversions
	 * and adds the object and his projected position to the Map<CelestialObject, CartesianCoordinates>
	 * 
	 * @param objects
	 * 			Celestial objects to be projected
	 * 
	 * @return array of double
	 */
	public double[] projectListCelestialObject(List<CelestialObject> objects, List<CartesianCoordinates> cartesianCoordinates) {

		int index = 0;
		double[] celestialObjectsPositions = new double[objects.size() * 2];

		for (CelestialObject object : objects) {
			
			HorizontalCoordinates horConverted = equToHor.apply(object.equatorialPos());
			CartesianCoordinates projectedCoordinates = projection.apply(horConverted);

			celestialObjectsPositions[index] = projectedCoordinates.x();
			celestialObjectsPositions[index + 1] = projectedCoordinates.y();
			cartesianCoordinates.add(projectedCoordinates);
			celestialCoordinates.put(object, projectedCoordinates);
			
			if(object.name().equals(starString))
				selectedStarCoordinates = projectedCoordinates;	

			index+=2;
		}

		return celestialObjectsPositions;
	}

	/**
	 * Fills the list of planets and returns the list of planets in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 * 
	 * @param daysSinceJ2010
	 * 			Number of days since the epoch J2010
	 * 
	 * @return list of planets in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 */
	private List<CelestialObject> constructPlanetList(double daysSinceJ2010) {

		List<CelestialObject> planetsCelestialObject = new ArrayList<CelestialObject>();

		for(PlanetModel planetMod : PlanetModel.ALL) {
			if (!planetMod.equals(PlanetModel.EARTH)) {
				Planet planet = planetMod.at(daysSinceJ2010, eclToEqu);
				planets.add(planet);
				planetsCelestialObject.add(planet);
			}	
		}

		return planetsCelestialObject;
	}

	/**
	 * Returns the list of stars in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 * 
	 * 
	 * @return list of stars in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 */
	private List<CelestialObject> starsCelestialObjects(StarCatalogue catalogue) {
		List<CelestialObject> starsCelestialObjects = new ArrayList<CelestialObject>();

		for (Star star : catalogue.stars()) {
			starsCelestialObjects.add(star);
		}

		return starsCelestialObjects;
	}

	/**
	 * Returns a cell containing either the closest celestial object to the point given 
	 * if the distance between the two is inferior to maxDistance
	 * or a empty cell if no celestial object has been found
	 * 
	 * @param coordinates
	 * 				 Coordinates of the point
	 * 
	 * @param maxDistance 
	 * 				 Maximal distance to the point
	 * 
	 * @throws NullPointerException
	 * 				 If the coordinates are null
	 * 
	 * @throws IllegalArgumentException
	 * 				If the maxDistance is negative
	 * 
	 * @return cell containing either the closest celestial object to the point given 
	 * 		   if the distance between the two is inferior to maxDistance
	 *		   or a empty cell if no celestial object has been found
	 */
	public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
		checkArgument(maxDistance>=0);
		requireNonNull(coordinates);

		List<CelestialObject> reducedList = new ArrayList<CelestialObject>(); 

		//First filter to avoid unnecessary computations : checks if the object is in the square of length 2*maxDistance centered in coordinates parameter 
		for(CelestialObject object : celestialCoordinates.keySet()) {
			boolean deltaX = abs(celestialCoordinates.get(object).x() - coordinates.x()) <= maxDistance;
			boolean deltaY = abs(celestialCoordinates.get(object).y() - coordinates.y()) <= maxDistance;

			if (deltaX && deltaY) {
				reducedList.add(object);
			}
		}

		double minDistance = Double.MAX_VALUE;
		Optional<CelestialObject> closest = null;
		//From the filtered list we can find the closest object
		for(CelestialObject object : reducedList) {
			double tempDistance = coordinates.distance(celestialCoordinates.get(object));

			if (tempDistance <= minDistance) {
				minDistance = tempDistance;
				closest = Optional.of(object);
			}
		}

		if (closest == null)
			closest = Optional.empty();

		return closest;
	}

	/**
	 * Returns the Sun calculated in the constructor
	 * 
	 * @return the Sun calculated in the constructor
	 */
	public Sun sun() {
		return sun;
	}

	/**
	 * Returns the coordinates of the Sun in the projected plan
	 * 
	 * @return Coordinates of the Sun in the projected plan
	 */
	public CartesianCoordinates sunPosition() {
		return sunPosition;
	}

	/**
	 * Getters for the list of the planets of the Solar System except planet Earth
	 * 
	 * @return Immutable copy of the list of the planets of the Solar System except planet Earth
	 */
	public List<Planet> planets() {
		return List.copyOf(planets);
	}

	/**
	 * Returns an array containing the cartesian coordinates components of the planets list 
	 * where the 2i-th and (2i+1)-th indexes corresponding respectively to x and y coordinates in the projected plan of the i-th planet
	 * 
	 * @return Copy of the array containing the described above cartesian coordinates of the planets 
	 */
	public double[] planetPositions() {
		return planetPositions.clone();
	}

	/**
	 * Getters for the list of the stars
	 * 
	 * @return Immutable copy of the list of the planets
	 */
	public List<Star> stars() {
		return catalogue.stars();
	}

	/**
	 * Returns an array containing the cartesian coordinates components of the star list 
	 * where the 2i-th and (2i+1)-th indexes corresponding respectively to x and y coordinates in the projected plan of the i-th star
	 * 
	 * @return Copy of the array containing the described above cartesian coordinates of the stars
	 */
	public double[] starPositions() {
		return starPositions.clone();
	}

	/**
	 * Returns immutable copy of the list of cartesian coordinates of the stars
	 * 
	 * @return immutable copy of the list of cartesian coordinates of the stars
	 */
	public List<CartesianCoordinates> starCartesianCoordinates() {
		return List.copyOf(starCartesianPositions);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public CartesianCoordinates pathCoordinates() {
		return selectedStarCoordinates;
	}

	/**
	 * Returns the Moon calculated in the constructor
	 * 
	 * @return the Moon calculated in the constructor
	 */
	public Moon moon() {
		return moon;
	}

	/**
	 * Returns the coordinates of the Moon in the projected plan
	 * 
	 * @return Coordinates of the Moon in the projected plan
	 */
	public CartesianCoordinates moonPosition() {
		return moonPosition;
	}

	/**
	 * Returns the set of asterisms of the StarCatalogue
	 * 
	 * @return Set of asterisms of the StarCatalogue
	 */
	public Set<Asterism> asterisms() {
		return catalogue.asterisms();
	}

	/**
	 * Returns the list of indices associated to the stars of the asterism
	 * 
	 * @param asterism 
	 * 				Asterism for which we want the list of indices
	 * 
	 * @return List of star indices related to the asterism
	 * 
	 */
	public List<Integer> asterismIndices(Asterism asterism) {
		return catalogue.asterismIndices(asterism);
	}
}
