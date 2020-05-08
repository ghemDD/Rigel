package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import static ch.epfl.rigel.coordinates.EquatorialCoordinates.of;
/**
 * Represents a loader for the Star Catalogue builder subclass
 *  
 * @author Nael Ouerghemi (310435)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader{
	INSTANCE;

	private HygDatabaseLoader() {}

	/**
	 * Method which interprets the data file and loads Star Catalogue with the list of Stars
	 */
	public void load(InputStream inputStream, Builder builder) throws IOException {

		try(BufferedReader b= new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));) {

			String currentLine;
			String[] data;

			//Skips first line
			b.readLine();

			//Initialization
			currentLine = b.readLine();

			while(currentLine!=null) {

				data = currentLine.split(",");
				int hip = data[Columns.HIP.ordinal()].equals("") ? 0 : Integer.parseInt(data[Columns.HIP.ordinal()]);

				StringBuilder build = new StringBuilder();
				String bayer = data[Columns.BAYER.ordinal()].isBlank() ? "?" : data[Columns.BAYER.ordinal()];
				String con = data[Columns.CON.ordinal()];

				String altName = build.append(bayer)
						.append(" ")
						.append(con)
						.toString();

				String raw = data[Columns.PROPER.ordinal()];
				String name = raw.isEmpty() ? altName : raw;

				double ra = data[Columns.RARAD.ordinal()].isEmpty() ? 0 : Double.parseDouble(data[Columns.RARAD.ordinal()]);
				double dec = data[Columns.DECRAD.ordinal()].isEmpty() ? 0 : Double.parseDouble(data[Columns.DECRAD.ordinal()]);

				float mag = data[Columns.MAG.ordinal()].isEmpty() ? 0 : Float.parseFloat(data[Columns.MAG.ordinal()]);
				float color = data[Columns.CI.ordinal()].isEmpty() ? 0 : Float.parseFloat(data[Columns.CI.ordinal()]);

				builder.addStar(new Star(hip, name, of(ra, dec), mag, color));

				currentLine = b.readLine();
			} 
		}
	}

	private enum Columns {
		ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
		RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
		RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
		COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
	}

}
