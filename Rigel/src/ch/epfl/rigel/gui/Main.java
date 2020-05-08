package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

public class Main extends Application {

	public static void main(String[] args) {launch(args);}
	
	/**
	 * 
	 * @param resourceName
	 * @return
	 */
	private InputStream resourceStream(String resourceName) {
	    return getClass().getResourceAsStream(resourceName);
	  }
	
	/**
	 * @see Object#Application()
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		try (InputStream hs = resourceStream("/hygdata_v3.csv");
		    	 InputStream ast = resourceStream("/asterisms.txt")) {
		      StarCatalogue catalogue = new StarCatalogue.Builder()
			.loadFrom(hs, HygDatabaseLoader.INSTANCE)
			.loadFrom(ast, AsterismLoader.INSTANCE)
			.build();
		}
		      
		     
		      
		HBox controlBar = new HBox();
		controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
		
		HBox observationLon = new HBox();
		observationLon.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		Label obsLabelLon = new Label("Longitude (°) :");
		observationLon.getChildren().add(obsLabelLon);
		TextField lonTextField = new TextField();
		formatCoorTextField(lonTextField, true);
		observationLon.getChildren().add(lonTextField);
		//Bind property
		
		HBox observationLat = new HBox();
		observationLat.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		Label obsLabelLat = new Label("Latitude (°) :");
		observationLat.getChildren().add(obsLabelLat);
		TextField latTextField = new TextField();
		formatCoorTextField(latTextField, false);
		observationLat.getChildren().add(latTextField);
		//Bind property
		
		
		//Instant d'observation
		//Bind property disable to property running of time animator
		//Date : LocalTime
		HBox observationDate = new HBox();
		observationDate.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
		Label obsLabelDate = new Label("Date :");
		DatePicker date = new DatePicker();
		date.setStyle("-fx-pref-width: 120;");
		//Bind property with date bean
		//Hour : LocalTime
		Label hour = new Label("Heure :");
		TextField hourTextField = new TextField();
		hourTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
		
		//Zone : ZoneID
		ComboBox<String> zone = new ComboBox<>();
		zone.setStyle("-fx-pref-width: 180;");
		List<String> zonesId = new ArrayList<String>(ZoneId.getAvailableZoneIds());
		Collections.sort(zonesId);
		zone.getItems().addAll(zonesId);
		
		//Time animator Pane
		HBox timeAnim = new HBox();
		timeAnim.setStyle("-fx-spacing: inherit;");
		
		
		
		 primaryStage.setMinWidth(800);
		 primaryStage.setMinHeight(600);
		 //primaryStage.setScene(new Scene(root));     
		      
		      
		}
	
		/**
		 * Format the text field given in parameter depending on the type of coordinates represented by the boolean longitude
		 * 
		 * @param coorTextField
		 * 				Text field to be formatted
		 * 
		 * @param longitude
		 * 				Boolean indicating if the text field represents longitude (if true) / latitude (if false)
		 * 
		 */
		private void formatCoorTextField(TextField coorTextField, boolean longitude) {
			NumberStringConverter stringConverter =
					new NumberStringConverter("#0.00");

			UnaryOperator<TextFormatter.Change> coorFilter = (change -> {
				try {
					String newText =
							change.getControlNewText();
					double newCoorDeg =
							stringConverter.fromString(newText).doubleValue();
					
					
					boolean lonCondition = GeographicCoordinates.isValidLonDeg(newCoorDeg);
					boolean latCondition = GeographicCoordinates.isValidLatDeg(newCoorDeg);
					
					
					return longitude ? (lonCondition ? change : null) : 
							  		   (latCondition ? change : null);
				
				} catch (Exception e) {
					return null;
				}
			});

			TextFormatter<Number> coorTextFormatter =
					new TextFormatter<>(stringConverter, 0, coorFilter);

			coorTextField.setTextFormatter(coorTextFormatter);
		}
		
		
		private void formatDateTextField() {
			DateTimeFormatter hmsFormatter =
					  DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalTimeStringConverter stringConverter =
					  new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
			TextFormatter<LocalTime> timeFormatter =
					  new TextFormatter<>(stringConverter);
		}

}
