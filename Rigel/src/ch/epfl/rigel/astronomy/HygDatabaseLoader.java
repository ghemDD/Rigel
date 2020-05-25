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

				String hip = data[Columns.HIP.ordinal()];
				int hipId = hip.isEmpty() ? 0 : Integer.parseInt(hip);
				
                String mag = data[Columns.MAG.ordinal()];
                float magni = mag.isEmpty() ? 0 : Float.parseFloat(mag);
                
                String index = data[Columns.CI.ordinal()];
                float color = index.isEmpty() ? 0 : Float.parseFloat(index);
                
                String bay = data[Columns.BAYER.ordinal()];
                String bayer = bay.isEmpty() ? "?" : bay;
                 
                String con = data[Columns.CON.ordinal()];
				
                StringBuilder build = new StringBuilder();
                String altName = build.append(bayer)
									  .append(" ")
									  .append(con)
									  .toString();

				String raw = data[Columns.PROPER.ordinal()];
				String name = raw.isEmpty() ? altName : raw;
				
				double ra = Double.parseDouble(data[Columns.RARAD.ordinal()]);
                double dec = Double.parseDouble(data[Columns.DECRAD.ordinal()]);

				builder.addStar(new Star(hipId, name, of(ra, dec), magni, color));

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
