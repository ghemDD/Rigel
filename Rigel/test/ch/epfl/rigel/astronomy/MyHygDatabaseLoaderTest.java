package ch.epfl.rigel.astronomy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class MyHygDatabaseLoaderTest {
	  private static final String HYG_CATALOGUE_NAME =
	    "/hygdata_v3.csv";
	  private static final String ASTERISM_CATALOGUE_NAME = 
			  "/asterisms.txt";

	  @Test
	  void hygDatabaseIsCorrectlyInstalled() throws IOException {
	    try (InputStream hygStream = getClass()
		   .getResourceAsStream(HYG_CATALOGUE_NAME)) {
	      assertNotNull(hygStream);
	    }
	  }

	  @Test
	  void hygDatabaseContainsRigel() throws IOException {
	    try (InputStream hygStream = getClass()
		   .getResourceAsStream(HYG_CATALOGUE_NAME);
		   InputStream astStream = getClass()
		   .getResourceAsStream(ASTERISM_CATALOGUE_NAME)) {
	      StarCatalogue catalogue = new StarCatalogue.Builder()
		.loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
		.loadFrom(astStream, AsterismLoader.INSTANCE)
		.build();
	      Star rigel = null;
	      for (Star s : catalogue.stars()) {
		if (s.name().equalsIgnoreCase("rigel"))
		  rigel = s;
	      }
	      
	      assertNotNull(rigel);
	      assertEquals(5067, catalogue.stars().size());
	      assertEquals(153, catalogue.asterisms().size());
	      
	      
	      for(Asterism asterism : catalogue.asterisms()) {
	    	  int nbIndices = catalogue.asterismIndices(asterism).size();
	    	  int nbStars = asterism.stars().size();
	    	  assertEquals(nbIndices, nbStars);
	      }
	    }
	  }
	  
		@Test
		void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
		    try (InputStream hygStream = getClass()
		            .getResourceAsStream(HYG_CATALOGUE_NAME)) {
		        InputStream asterismStream = getClass()
		                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
		        StarCatalogue catalogue = new StarCatalogue.Builder()
		                .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
		                .build();
		        Star rigel = null;
		        for (Star s : catalogue.stars()) {
		            if (s.name().equalsIgnoreCase("rigel"))
		                rigel = s;
		        }
		        assertNotNull(rigel);

		        List<Star> allStar = new ArrayList<Star>();
		        allStar.addAll(catalogue.stars());

		        System.out.println("LIST OF STARS :");
		        for(Star s : allStar){
		            System.out.print(s.hipparcosId() + " ");
		        } //should print out the same star IDS as in the fichier (check visually)
		        System.out.println();
		        System.out.println();

		        System.out.println("ASTERISMS : ");
		        int i;

		        //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
		        //flemme de coder une méthode qui vérifie automatiquement
		        for(Asterism asterism : catalogue.asterisms()){
		            List<Integer> cAstInd = catalogue.asterismIndices(asterism);
		            i = 0;
		            for(Star star : asterism.stars()){
		                System.out.print("Hip : ");
		                System.out.print(star.hipparcosId());
		                System.out.print("  foundHipparcos : ");
		                System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

		                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
		                l'index stoqué dans l'astérisme voulu : */
		                assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
		                System.out.print(" ||| ");
		                i++;
		            }
		            System.out.println();
		        }
		    }
		}
	}