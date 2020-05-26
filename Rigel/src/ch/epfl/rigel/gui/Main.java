package ch.epfl.rigel.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

public final class Main extends Application {
	private StringBinding objectUnderMouseString;
	
	private ObjectBinding<ZoneId> boxZoneId;
	
	private DatePicker datePicker;
	private TextFormatter<Number> latTextFormatter;
	private TextFormatter<Number> lonTextFormatter;
	private TextFormatter<LocalTime> timeFormatter;
	private DateTimeBean dateTimeBean;
	
	private ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>(
			NamedTimeAccelerator.TIMES_300.getAccelerator());

	private TimeAnimator timeAnimator;

	private static final List<String> SORTED_ZONEIDS =  sortedZoneIds();
	
	 private InputStream resourceStream(String resourceName) {
		    return getClass().getResourceAsStream(resourceName);
		  }
	
	public static void main(String[] args) { launch(args); }
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		try (InputStream hs = resourceStream("/hygdata_v3.csv");
			 InputStream ast = resourceStream("/asterisms.txt")) {
		      StarCatalogue catalogue = new StarCatalogue.Builder()
			.loadFrom(hs, HygDatabaseLoader.INSTANCE)
			.loadFrom(ast, AsterismLoader.INSTANCE)
			.build();
		      

		      
		      
	        ZonedDateTime when = ZonedDateTime.now();
			dateTimeBean = new DateTimeBean();
			dateTimeBean.setZonedDateTime(when);
			
			timeAnimator = new TimeAnimator(dateTimeBean);
			timeAnimator.setAccelerator(NamedTimeAccelerator.TIMES_300.getAccelerator());
			
			accelerator.addListener((p,o,n) -> timeAnimator.setAccelerator(n));

			ObserverLocationBean observerLocationBean = new ObserverLocationBean();
		    observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));
	
		    ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
			viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
				      viewingParametersBean.setFieldOfViewDeg(100);
		      
		      
			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			
			
			HBox controlBar = new HBox();
			controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
	
			HBox obsPosBox = new HBox();
			obsPosBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
			
			
			Label lonLabel = new Label("Longitude (°) :");
	
			// a modulariser après
			NumberStringConverter stringConverter =
					  new NumberStringConverter("#0.00");
			UnaryOperator<TextFormatter.Change> lonFilter = (change -> {
			    try {
			      String newText =
				change.getControlNewText();
			      double newLonDeg =
				stringConverter.fromString(newText).doubleValue();
			      return GeographicCoordinates.isValidLonDeg(newLonDeg)
				? change
				: null;
			    } catch (Exception e) {
			      return null;
			    }
			  });
	
			lonTextFormatter =
			  new TextFormatter<>(stringConverter, 0, lonFilter);
			lonTextFormatter.valueProperty().setValue(6.57);
			observerLocationBean.getLonDegProperty().bind(lonTextFormatter.valueProperty());
			
			TextField lonTextField =
			  new TextField();
			
			
			lonTextField.setTextFormatter(lonTextFormatter);		
					
			lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;" );
			
			
			obsPosBox.getChildren().addAll(lonLabel, lonTextField);
			
			
			Label latLabel = new Label("Latitude (°) :");
	
			UnaryOperator<TextFormatter.Change> latFilter = (change -> {
			    try {
			      String newText =
				change.getControlNewText();
			      double newLatDeg =
				stringConverter.fromString(newText).doubleValue();
			      return GeographicCoordinates.isValidLatDeg(newLatDeg)
				? change
				: null;
			    } catch (Exception e) {
			      return null;
			    }
			  });
	
			latTextFormatter =
			  new TextFormatter<>(stringConverter, 0, latFilter);
	
			observerLocationBean.getLatDegProperty().bind(latTextFormatter.valueProperty());

			latTextFormatter.valueProperty().setValue(46.52);

			
			TextField latTextField =
			  new TextField();
			latTextField.setTextFormatter(latTextFormatter);
			
				
			
			latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;" );
			
			
			obsPosBox.getChildren().addAll(latLabel, latTextField);
			
			
			HBox whenBox = new HBox();
			whenBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;" );
			
			Label dateLabel = new Label("Date: ");
			
			datePicker = new DatePicker();
			dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
			datePicker.valueProperty().set(when.toLocalDate());
			datePicker.setStyle("-fx-pref-width: 120");
			
			
			
			Label hourLabel = new Label("Heure: ");
			
			
			
			DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalTimeStringConverter hourStringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
			timeFormatter = new TextFormatter<>(hourStringConverter);
			
			timeFormatter.valueProperty().setValue(when.toLocalTime());
			
			dateTimeBean.timeProperty().bindBidirectional(timeFormatter.valueProperty());
			
			TextField hourTextField = new TextField();
			hourTextField.setTextFormatter(timeFormatter);
			hourTextField.setStyle("-fx-pref-width: 75;  -fx-alignment: baseline-right;");
			
			
			ComboBox<String> zoneIdRoll = new ComboBox<String>();
			
			zoneIdRoll.valueProperty().set(when.getZone().toString());;
			zoneIdRoll.getItems().addAll(SORTED_ZONEIDS);
			zoneIdRoll.setStyle(" -fx-pref-width: 180;");
			
			boxZoneId = 
					Bindings.createObjectBinding(() ->  ZoneId.of(zoneIdRoll.valueProperty().get()) , zoneIdRoll.valueProperty());
			dateTimeBean.zoneProperty().bind(boxZoneId);
			
			whenBox.getChildren().addAll(dateLabel, datePicker, hourLabel, hourTextField, zoneIdRoll);
			
			whenBox.disableProperty().bind(timeAnimator.getRunning());
			
			
			HBox timeBox = new HBox();
			timeBox.setStyle("-fx-spacing: inherit;");
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			ComboBox<NamedTimeAccelerator> acceleratorRoll = new ComboBox<NamedTimeAccelerator>();
			acceleratorRoll.setValue(NamedTimeAccelerator.TIMES_300);
			List<NamedTimeAccelerator> listAccelerator = Arrays.asList(NamedTimeAccelerator.values());
			acceleratorRoll.setItems(javafx.collections.FXCollections.observableList(listAccelerator));
			
			
			accelerator.bind(Bindings.select(acceleratorRoll.valueProperty(), "accelerator" ));
			
			
			
			
			
			
			
			
			
			
			
		
			try(InputStream fontStream = getClass()
					  .getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");){
				Font fontAwesome = Font.loadFont(fontStream, 15);
				
				
				String resetString = "\uf0e2";
				String playString = "\uf04b";
				String pauseString = "\uf04c";
				
				Button resetButton = new Button(resetString);
				resetButton.setFont(fontAwesome);
				
				resetButton.setOnMouseClicked(e ->{
				unBindAllDateTimeBean();
				dateTimeBean.setZonedDateTime(when);
				bindAllDateTimeBean();
				});
				
				Button playButton = new Button(playString);
				playButton.setFont(fontAwesome);
				
				playButton.setOnMouseClicked(e-> {
				unBindAllDateTimeBean();
				timeAnimator.start();}
						);
				
				Button pauseButton = new Button(pauseString);
				pauseButton.setFont(fontAwesome);
				
				pauseButton.setOnMouseClicked(e-> {
					bindAllDateTimeBean();
					timeAnimator.stop();
				});

				
				timeBox.getChildren().addAll(acceleratorRoll, resetButton, playButton, pauseButton);
				fontStream.close();
			} catch( IOException e) {
				  throw new UncheckedIOException(e);
			}
			
			
			
			Separator separator = new Separator();
			Separator separator1 = new Separator();
			controlBar.getChildren().addAll(obsPosBox, separator, whenBox,separator1, timeBox);
			
			
			
			System.out.println("catalogue: " + catalogue.toString());
	
			System.out.println("dateTimeBean: " + dateTimeBean.getZonedDateTime().toString());
			
			System.out.println("observerLocationBean: " + observerLocationBean.getGeographicCoordinates().toString());

			
			SkyCanvasManager canvasManager = new SkyCanvasManager(
					catalogue,
					dateTimeBean,
					observerLocationBean,
					viewingParametersBean);
	
			Canvas sky = canvasManager.canvas();
			Pane skyPane = new Pane();
					
			skyPane.getChildren().addAll(sky);
			BorderPane root = new BorderPane(skyPane);
	
			sky.widthProperty().bind(root.widthProperty());
			sky.heightProperty().bind(root.heightProperty());
	
	
			
			
			
			
			
			Text fieldOfViewText = new Text();
			
			
		
			fieldOfViewText.textProperty().setValue("Champ de vue: 100°");
			
			fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", 
					viewingParametersBean.getFieldOfViewDegProperty()));
			
		    objectUnderMouseString = Bindings.createStringBinding(
					() -> canvasManager.objectUnderMouseProperty().getValue().info(), canvasManager.objectUnderMouseProperty());
			
			Text closestObjectText = new Text();
			
			closestObjectText.textProperty().bind(objectUnderMouseString);
			
			Text mousePosition = new Text();
			mousePosition.textProperty().bind(Bindings.format("Azimut : %.1f°, hauteur : %.1f°", 
							canvasManager.mouseAzDegProperty(), 
							canvasManager.mouseAltDegProperty()));
			
			
			BorderPane informationBar = new BorderPane(closestObjectText, null,mousePosition ,null, fieldOfViewText )		;	
			informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");
			
	        BorderPane mainPane = new BorderPane(root, controlBar, null, informationBar, null);

	        
	        primaryStage.setScene(new Scene(mainPane));
	        primaryStage.setTitle("Rigel");
	        
	        Image icon = new Image("file:excla.png");
	        primaryStage.getIcons().add(icon);
			
			primaryStage.show();
			
			sky.requestFocus();
			
		}
	}
	
	
	private final static List<String> sortedZoneIds() {
		
		List<String> IdsList = new ArrayList<String>();
		IdsList.addAll(ZoneId.getAvailableZoneIds());
		java.util.Collections.sort(IdsList);
		return  IdsList;

	}
	
	
	private void bindAllDateTimeBean() {
		dateTimeBean.timeProperty().bindBidirectional(timeFormatter.valueProperty());
		dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
		dateTimeBean.zoneProperty().bind(boxZoneId);

	}
	private void unBindAllDateTimeBean() {
		dateTimeBean.timeProperty().unbind();
		dateTimeBean.dateProperty().unbind();
		dateTimeBean.zoneProperty().unbind();

	}
	
}