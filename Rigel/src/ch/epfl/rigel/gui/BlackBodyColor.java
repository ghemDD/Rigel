package ch.epfl.rigel.gui;

import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
	private static final String COLOR_PATH = "/bbr_color.txt";

	private BlackBodyColor() {}

	/**
	 * Builds the Map<Integer, String> mapping the temperature of the Star and the subsequent color given the resource file
	 *  
	 * @return Map<Integer, String> mapping the temperature of the Star and the subsequent color
	 */
	private static Map<Integer, String> tempColorMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		try (InputStream inputStream = BlackBodyColor.class
				.getResourceAsStream(COLOR_PATH)) {
			InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader buffered = new BufferedReader(reader);
			String currentLine = buffered.readLine();

			while(currentLine!=null) {
				if (!ignoreLine(currentLine)) {
					int temp = 0;
					temp = Integer.parseInt(currentLine.substring(1, 6).trim());
					map.put(temp, currentLine.substring(80, 87));
				}
				currentLine = buffered.readLine();
			}

			return map;

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
	 * 				String parameter
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
	 * @throws IllegalArgumentException
	 * 			If the temperature is not in the closed interval [1000, 40000]
	 * 			Or if the map is empty
	 * 
	 * @return Color given the temperature of the Star
	 */
	public static Color colorForTemperature(double temperature) {
		checkArgument(!MAP.isEmpty());
		checkInInterval(COLOR_INT, temperature);

		int tempMultiple = (int) (Math.round(temperature/100.0)*100);

		String hexString = MAP.get(tempMultiple);
		Color color = Color.web(hexString);

		return color;
	}
}
