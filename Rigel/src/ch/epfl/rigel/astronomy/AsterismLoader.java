package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;

/**
 * Represents a loader for the Star Catalogue builder subclass
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum AsterismLoader implements StarCatalogue.Loader {
	INSTANCE;

	private AsterismLoader() {}

	/**
	 * Method which interprets the data file and loads Star Catalog Builder with the list of asterisms
	 */
	public void load(InputStream inputStream, Builder builder) throws IOException {

		try (BufferedReader b= new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));) {
			String currentLine;
			String[] data;
			List<Star> listars = new ArrayList<Star>();

			currentLine = b.readLine();

			do {

				listars.clear();
				data = currentLine.split(",");

				for(int y=0; y<data.length; ++y) {
					int index = Integer.parseInt(data[y]);
					listars.add(builder.map().get(index));
				}

				builder.addAsterism(new Asterism(listars));
				currentLine = b.readLine();

			} while(currentLine!=null);
		}
	}

}
