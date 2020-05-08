package ch.epfl.rigel.astronomy;


import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialToHorizontalConversion;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import static ch.epfl.rigel.math.EuclideanDistance.*;

/**
 * Represents the set of all celestial objects visible in the sky
 * 
 * @author Nael Ouerghemi (310435)
 */
public class ObservedSky {

	private List<CelestialObject> celestialObjects;

	private List<Planet> planets;
	private List<CartesianCoordinates> planetCartesianPositions;
	private double[] planetPositions;

	private StereographicProjection projection;
	private EclipticToEquatorialConversion eclToEqu;
	private EquatorialToHorizontalConversion equToHor;
	Function<EclipticCoordinates,HorizontalCoordinates> eclToHor;

	private Sun sun;
	private CartesianCoordinates sunPosition;

	private Moon moon;
	private CartesianCoordinates moonPosition;

	private StarCatalogue catalogue;
	private List<CartesianCoordinates> starCartesianPositions;
	private double[] starPositions;

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
	public ObservedSky(ZonedDateTime date, GeographicCoordinates observationPosition, StereographicProjection stereo, StarCatalogue catalogue) {
		
		/**
		 * Coordinates conversions
		 */
		double daysSinceJ2010 = Epoch.J2010.daysUntil(date);
		eclToEqu = new EclipticToEquatorialConversion(date);
		equToHor = new EquatorialToHorizontalConversion(date, observationPosition);
		projection = stereo;
		celestialObjects = new ArrayList<CelestialObject>();
		planets = new ArrayList<Planet>();
		planetCartesianPositions = new ArrayList<>();
		starCartesianPositions = new ArrayList<>();
		this.catalogue = catalogue;

		eclToHor = eclToEqu.andThen(equToHor);

		/**
		 * Sun position
		 */
		sun = SunModel.SUN.at(daysSinceJ2010, eclToEqu);
		sunPosition = projectSingleCelestialObject(sun);
		
		/**
		//sunPosition = stereo.apply(eclToHor.apply(sun.eclipticPos()));
		//celestialObjects.add(sun);
		 */

		/**
		 * Moon position
		 */

		moon = MoonModel.MOON.at(daysSinceJ2010, eclToEqu);
		moonPosition = projectSingleCelestialObject(moon);
		
		/**
		//moonPosition = stereo.apply(equToHor.apply(moon.equatorialPos()));
		//celestialObjects.add(moon);
		 */

		/**
		 * Planets positions
		 */
		
		List<CelestialObject> planetsCelestial = constructPlanetList(daysSinceJ2010);
		planetPositions = projectListCelestialObject(planetsCelestial, planetCartesianPositions);
		
		/**
		int index = 0;
		int y = 0;
		planetPositions = projectListCelestialObject(toArrayCelestialObject(planets));
		planetPositions = new double[2 * (PlanetModel.ALL.size()-1)];
		 **/
		
		/**
		 * Star list and positions
		 */
		
		List<CelestialObject> starsCelestial = starsCelestialObjects(catalogue);
		starPositions = projectListCelestialObject(starsCelestial, starCartesianPositions);
		
		/**
		index=0;
		starPositions = new double[2 * catalogue.stars().size()];

		for(int i = 0; i < catalogue.stars().size(); ++i) { 
			HorizontalCoordinates conv = equToHor.apply(catalogue.stars().get(i).equatorialPos());
			starCartesianPositions.add(stereo.apply(conv));

			starPositions[index]= starCartesianPositions.get(i).x();
			starPositions[index+1] = starCartesianPositions.get(i).y();
			
			if (catalogue.stars().get(i).name().equals("Betelgeuse")) { 
				System.out.println("Betelgeuse "+starCartesianPositions.get(i));
			}
			//System.out.print(catalogue.stars().get(i).name()+" ");
			//System.out.print(catalogue.stars().get(i).equatorialPos()+" ");
			//System.out.print(" azDeg "+conv.azDeg()+" altDeg "+conv.altDeg());
			//System.out.println(" x : "+starPositions[index]+" y : "+starPositions[index+1]);
			//System.out.println(starCartesianPositions.get(i).toString());

			starMap.put(catalogue.stars().get(i), starCartesianPositions.get(i));
			
			index +=2;
		}

		celestialObjects.addAll(catalogue.stars());
		
		**/
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
		
		celestialObjects.add(object);
		
		return projCartCoordinates;
	}
	
	/**
	 * Projects a list of celestial objects depending on the stereographic projection attribute and conversions
	 * and adds the object to the list of celestial objects
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
			celestialObjects.add(object);
			
			index+=2;
			
		}
		
		return celestialObjectsPositions;
	}

	/**
	 * Fills the list of planets and returns the list of planets in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 * and adds the list of planets to the list of celestialObjects
	 * @param daysSinceJ2010
	 * 			Number of days since the epoch J2010
	 * 
	 * @return list of planets in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 */
	private List<CelestialObject> constructPlanetList(double daysSinceJ2010) {
		
		List<CelestialObject> planetsCelestialObject = new ArrayList<CelestialObject>();
		
		for(int i = 0; i < PlanetModel.ALL.size(); ++i) {
			if (!PlanetModel.ALL.get(i).equals(PlanetModel.EARTH)) {
				Planet planet = PlanetModel.ALL.get(i).at(daysSinceJ2010, eclToEqu);
				planets.add(planet);
				planetsCelestialObject.add(planet);

				//planetCartesianPositions.add(stereo.apply(equToHor.apply(planet.equatorialPos())));
				//planetPositions[index] = planetCartesianPositions.get(y).x();
				//planetPositions[index+1] = planetCartesianPositions.get(y).y();
				//index+=2;
				//++y;
			}	
		}

		celestialObjects.addAll(planets);
		
		return planetsCelestialObject;
	}
	
	/**
	 * Returns the list of stars in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 * and the list of stars to the list of celestial objects
	 * 
	 * @return list of stars in the form of a List<CelestialObject> to be used in further method projectListCelestialObject
	 */
	private List<CelestialObject> starsCelestialObjects(StarCatalogue catalogue) {
		List<CelestialObject> starsCelestialObjects = new ArrayList<CelestialObject>();
		
		for (Star star : catalogue.stars()) {
			starsCelestialObjects.add(star);
		}
		
		celestialObjects.addAll(catalogue.stars());
		
		return starsCelestialObjects;
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
	 * 
	 * @return
	 */
	public List<CartesianCoordinates> planetCartesianCoordinates() {
		return planetCartesianPositions;
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
	 * @return immutable copy of the list of cartesian coordinates of the stars
	 */
	public List<CartesianCoordinates> starCartesianCoordinates() {
		return List.copyOf(starCartesianPositions);
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
	 * @throws IllegalArgumentException 
	 * 				if asterism given is not in the catalogue
	 */
	public List<Integer> asterismIndices(Asterism asterism) {
		return catalogue.asterismIndices(asterism);
	}

	/**
	 * Returns a cell containing either the closest celestial object to the point given 
	 * if the distance between the two is inferior to maxDistance
	 * or a empty cell if no celestial object has been found
	 * 
	 * @param coordinates
	 * 				 Coordinates of the point
	 * @param maxDistance 
	 * 				 Maximal distance to the point
	 * 
	 * @return cell containing either the closest celestial object to the point given 
	 * 		   if the distance between the two is inferior to maxDistance
	 *		   or a empty cell if no celestial object has been found
	 */
	public Optional<CelestialObject> objectClosestTo(CartesianCoordinates coordinates, double maxDistance) {
		//Unify all the celestial objects
		//First filter of celestial objects 
		//Less computation of euclidean distance reduce
		
		double minDistance = maxDistance;
		Optional<CelestialObject> closest = null;

		/**
		 * Sun
		 */
		//System.out.println("Sun position "+sunPosition());
		//System.out.println("Coordinates "+coordinates);
		double sunDistance = cartesianDistance(coordinates, sunPosition());

		if ( sunDistance <= minDistance) {
			minDistance = sunDistance;
			closest = Optional.of(sun());
		}

		/**
		 * Moon
		 */

		double moonDistance = cartesianDistance(coordinates, moonPosition());

		if (moonDistance <= minDistance) {
			minDistance = moonDistance;
			closest = Optional.of(moon());
		}

		/**
		 * Planets : checks if a Planet of the solar System is closer to the point than the closest CelestialObject
		 * or maximal distance if no celestial object has been found closer to the maximal distance
		 */

		for(int i=0; i<planets.size(); ++i) {
			CartesianCoordinates tempCoor = planetCartesianPositions.get(i);
			double tempDistance = cartesianDistance(coordinates, tempCoor);

			if (tempDistance <= minDistance) {
				minDistance = tempDistance;
				closest = Optional.of(planets.get(i));
			}

		}

		/**
		 * Stars : checks if a Star is closer to the point than the closest CelestialObject
		 * or maximal distance if no celestial object has been found closer to the maximal distance
		 */

		for(int i=0; i<starCartesianPositions.size(); ++i) {
			CartesianCoordinates tempCoor = starCartesianPositions.get(i);
			double tempDistance = cartesianDistance(coordinates, tempCoor);
			
			if (catalogue.stars().get(i).name().equals("Betelgeuse")) {
				System.out.println("Betelgeuse "+tempCoor);
				System.out.println("Mouse Position "+coordinates); 
			}

			if (tempDistance <= minDistance) {
				minDistance = tempDistance;
				closest = Optional.of(stars().get(i));
			}
		}

		/**
		 * If no celestial objects have been found closer to the maximal Distance
		 */
		if (closest==null)
			closest = Optional.empty();

		return closest;
	}
}
