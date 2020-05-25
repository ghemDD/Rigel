package ch.epfl.rigel.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

/**
 * Converts the temperature of a Star to a Color
 *  
 * @author Nael Ouerghemi (310435)
 */
public class BlackBodyColor {
	private static final ClosedInterval COLOR_INT = ClosedInterval.of(1000, 40000);
	private static final Map<Integer, String> MAP = tempColorMap();

	private BlackBodyColor(){}

	/**
	 * Builds the Map<Integer, String> mapping the temperature of the Star and the subsequent color given the resource file
	 *  
	 * @return Map<Integer, String> mapping the temperature of the Star and the subsequent color
	 */
	private static Map<Integer, String> tempColorMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		try (InputStream inputStream= BlackBodyColor.class
				.getResourceAsStream("/bbr_color.txt")) {
			InputStreamReader r= new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader b = new BufferedReader(r);
			String currentLine = b.readLine();

			while(currentLine!=null) {
				if (!ignoreLine(currentLine)) {
					int temp = 0;
					temp = Integer.parseInt(currentLine.substring(1, 6).trim());
					map.put(temp, currentLine.substring(80, 87));
				}
				currentLine = b.readLine();
			}

			return Map.copyOf(map);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Checks if a line has to be ignored : 
	 *  - if it starts with '#'
	 *  - if it contains "10deg"
	 *  
	 * @param string
	 * 				Line 
	 * 
	 * @return Returns true if line has to be ignored, false otherwise 
	 */
	private static boolean ignoreLine(String string) {
		return (string.charAt(0)=='#' || string.contains("2deg"));
	}

	/**
	 * Returns the Color for the given temperature
	 * 
	 * @param temperature 
	 * 				Temperature of the Star
	 * 
	 * @return Color given the temperature of the Star
	 */
	public static Color colorForTemperature(double temperature) {
		checkInInterval(COLOR_INT, temperature);

		int tempMultiple = (int) (Math.round(temperature/100.0)*100);

		String hexString = MAP.get(tempMultiple);
		Color color = Color.web(hexString);

		return color;
	}
}
