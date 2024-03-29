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
import java.util.function.Predicate;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.SepiaTone;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * Main class of the program
 * 
 * @author Tanguy Marbot (316756)
 * @author Nael Ouerghemi (310435)
 */
public final class Main extends Application {

	//SKY INFO
	private StringBinding objectUnderMouseString;
	private SkyCanvasManager canvasManager;

	//FORMATTERS
	private TextFormatter<Number> latTextFormatter;
	private TextFormatter<Number> lonTextFormatter;
	private TextFormatter<LocalTime> timeFormatter;
	private DatePicker datePicker;

	//BEANS
	private DateTimeBean dateTimeBean;
	private ObserverLocationBean observerLocationBean;
	private ViewingParametersBean viewingParametersBean;

	//DATE TIME BEAN
	private ZonedDateTime when;
	private ComboBox<String> zoneIdRoll;
	private ComboBox<String> starNamesRoll;
	private ObjectBinding<ZoneId> boxZoneId;
	private ObjectBinding<String> starBox;

	//TIME ANIMATOR
	private boolean play; 
	private ObjectProperty<TimeAccelerator> accelerator;
	private TimeAnimator timeAnimator;

	//VALUES ON START
	private static final double START_LON_GEO = 6.57 ;
	private static final double START_LAT_GEO = 46.52 ;
	private static final double START_AZ_HOR = 180.000000000001;
	private static final double START_ALT_HOR = 15.0;
	private static final double START_FIELD_OF_VIEW_DEG = 100.0;

	//CANVAS
	private static final double MIN_WIDTH = 800;
	private static final double MIN_HEIGHT = 600;

	//FONTS
	private static final String RESET_STRING = "\uf0e2";
	private static final String PLAY_STRING = "\uf04b";
	private static final String PAUSE_STRING = "\uf04c";
	private static final String COG_STRING = "\uf013";
	private static final String PEN_IDLE_STRING = "\uf303";
	private static final String PEN_ACTIVE_STRING = "\uf304";
	private static final String ERASER_STRING = "\uf12d";

	//FILES PATH
	private static final String STARS_PATH = "/hygdata_v3.csv";
	private static final String AST_PATH = "/asterisms.txt";

	private static final List<String> SORTED_ZONEIDS = sortedZoneIds();
	private List<String> listStars;

	//PATH
	private boolean trace;

	//Stage
	private Stage primaryStage;
	private BorderPane skyRoot;

	/**
	 * Sort the ID zones in natural order
	 * 
	 * @return ID zones sorted list
	 */
	private final static List<String> sortedZoneIds() {	
		List<String> IdsList = new ArrayList<String>();
		IdsList.addAll(ZoneId.getAvailableZoneIds());
		java.util.Collections.sort(IdsList);
		return  IdsList;
	}

	/**
	 * Main method of the application
	 * 
	 * @param args
	 * 			Parameters given at launch
	 */
	public static void main(String[] args) { launch(args); }

	/**
	 * This method sets all nodes and initializes all the properties used in the application
	 * 
	 * @throws Exception
	 * 			If an exception is found in the application
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try (InputStream hs = resourceStream(STARS_PATH);
				InputStream ast = resourceStream(AST_PATH)) {

			resetStartProcess();
			this.primaryStage = primaryStage;

			HBox whenBox = dateControlBar();

			//SKY PANE
			StarCatalogue catalogue = new StarCatalogue.Builder()
					.loadFrom(hs, HygDatabaseLoader.INSTANCE)
					.loadFrom(ast, AsterismLoader.INSTANCE)
					.build();

			listStars = catalogue.getStarNames();



			canvasManager = new SkyCanvasManager(
					catalogue,
					dateTimeBean,
					observerLocationBean,
					viewingParametersBean);

			Canvas sky = canvasManager.canvas();
			Pane skyPane = new Pane();		
			skyPane.getChildren().addAll(sky);
			skyRoot = new BorderPane(skyPane);
			sky.widthProperty().bind(skyRoot.widthProperty());
			sky.heightProperty().bind(skyRoot.heightProperty());

			//TOP CONTROL BAR
			HBox controlBar = new HBox();
			controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");

			HBox obsPosBox = coordControlBar();
			HBox timeBox = timeControlBar();

			Separator separator = new Separator();
			separator.setOrientation(Orientation.VERTICAL);

			Separator separatorSec = new Separator();
			separatorSec.setOrientation(Orientation.VERTICAL);

			controlBar.getChildren().addAll(obsPosBox, separator, whenBox, separatorSec, timeBox);

			//BOTTOM CONTROL BAR
			BorderPane informationBar = infoBar();

			//MAIN PANE
			BorderPane mainPane = new BorderPane(skyRoot, controlBar, null, informationBar, null);

			primaryStage.setMinWidth(MIN_WIDTH);
			primaryStage.setMinHeight(MIN_HEIGHT);
			primaryStage.setScene(new Scene(mainPane));
			primaryStage.setTitle("Rigel");
			primaryStage.show();

			sky.requestFocus();
		} 
	}

	private InputStream resourceStream(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}

	/**
	 * Method resetting the conditions of observation : viewing parameters, acceleration and observer's informations
	 */
	private void resetStartProcess() {
		play = true;
		when = ZonedDateTime.now();
		dateTimeBean = new DateTimeBean();
		dateTimeBean.setZonedDateTime(when);
		accelerator = new SimpleObjectProperty<>(NamedTimeAccelerator.TIMES_300.getAccelerator());

		timeAnimator = new TimeAnimator(dateTimeBean);
		timeAnimator.setAccelerator(NamedTimeAccelerator.TIMES_300.getAccelerator());	
		accelerator.addListener((p,o,n) -> timeAnimator.setAccelerator(n));

		observerLocationBean = new ObserverLocationBean();
		observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(START_LON_GEO, START_LAT_GEO));

		viewingParametersBean = new ViewingParametersBean();
		viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(START_AZ_HOR, START_ALT_HOR));
		viewingParametersBean.setFieldOfViewDeg(START_FIELD_OF_VIEW_DEG);
	}


	/**
	 * Instantiate the HBox corresponding to the control of coordinates bar, part of the top control bar
	 * 
	 * @return the HBox corresponding to the control of coordinates bar 
	 */
	private HBox coordControlBar() {

		HBox obsPosBox = new HBox();
		obsPosBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

		Label lonLabel = new Label("Longitude (°) :");
		lonTextFormatter = createNumberFormatter(GeographicCoordinates::isValidLonDeg);
		lonTextFormatter.valueProperty().setValue(START_LON_GEO);
		TextField lonTextField = new TextField();
		lonTextField.setTextFormatter(lonTextFormatter);				
		lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;" );

		Label latLabel = new Label("Latitude (°) :");
		latTextFormatter = createNumberFormatter(GeographicCoordinates::isValidLatDeg);
		latTextFormatter.valueProperty().setValue(START_LAT_GEO);
		TextField latTextField = new TextField();
		latTextField.setTextFormatter(latTextFormatter);
		latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");

		bindAllobserverLocationBean();
		obsPosBox.getChildren().addAll(lonLabel, lonTextField, latLabel, latTextField);

		return obsPosBox;
	}

	/**
	 * Binds all observer location bean properties
	 */
	private void bindAllobserverLocationBean() {
		observerLocationBean.lonDegProperty().bind(lonTextFormatter.valueProperty());
		observerLocationBean.latDegProperty().bind(latTextFormatter.valueProperty());
	}

	/**
	 * Creates a number formatter given the predicate
	 * 
	 * @return number formatter given the predicate
	 */
	private TextFormatter<Number> createNumberFormatter(Predicate<Double> predicate) {
		NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
		UnaryOperator<TextFormatter.Change> coorFilter = (change -> {
			try {
				String newText =
						change.getControlNewText();
				double newCoorDeg =
						stringConverter.fromString(newText).doubleValue();
				return predicate.test(newCoorDeg)
						? change
								: null;
			} catch (Exception e) {
				return null;
			}
		});

		return new TextFormatter<>(stringConverter, 0, coorFilter);
	}

	/**
	 * Instantiate the HBox corresponding to the date control bar, part of the top control bar
	 * 
	 * @return the HBox corresponding to the date control bar 
	 */
	private HBox dateControlBar() {
		HBox whenBox = new HBox();
		whenBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;" );
		// The date label allows us to choose a date
		Label dateLabel = new Label("Date: ");

		datePicker = new DatePicker();
		dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
		datePicker.valueProperty().set(when.toLocalDate());
		datePicker.setStyle("-fx-pref-width: 120");

		// The hour label allows us to choose an hour
		Label hourLabel = new Label("Heure: ");

		DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTimeStringConverter hourStringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);

		timeFormatter = new TextFormatter<>(hourStringConverter);	
		timeFormatter.valueProperty().setValue(when.toLocalTime());
		dateTimeBean.timeProperty().bindBidirectional(timeFormatter.valueProperty());

		TextField hourTextField = new TextField();
		hourTextField.setTextFormatter(timeFormatter);
		hourTextField.setStyle("-fx-pref-width: 75;  -fx-alignment: baseline-right;");

		// The zone id roll allows us to choose a Time zone

		zoneIdRoll = new ComboBox<String>();

		zoneIdRoll.valueProperty().set(when.getZone().toString());
		zoneIdRoll.getItems().addAll(SORTED_ZONEIDS);
		zoneIdRoll.setStyle(" -fx-pref-width: 180;");

		boxZoneId = Bindings.createObjectBinding(() ->  ZoneId.of(zoneIdRoll.valueProperty().get()) , zoneIdRoll.valueProperty());
		dateTimeBean.zoneProperty().bind(boxZoneId);

		whenBox.getChildren().addAll(dateLabel, datePicker, hourLabel, hourTextField, zoneIdRoll);
		//we make sure to disable any timeBox during an Animation
		whenBox.disableProperty().bind(timeAnimator.getRunning());

		return whenBox;
	}

	/**
	 * Instantiate the HBox corresponding to the time control bar, part of the top control bar
	 * 
	 * @return the HBox corresponding to the time control bar 
	 */
	private HBox timeControlBar() {
		HBox timeBox = new HBox();
		timeBox.setStyle("-fx-spacing: inherit;");		

		// The accelerator roll allows us to choose a type of accelerator
		ComboBox<NamedTimeAccelerator> acceleratorRoll = new ComboBox<NamedTimeAccelerator>();
		acceleratorRoll.setValue(NamedTimeAccelerator.TIMES_300);
		List<NamedTimeAccelerator> listAccelerator = Arrays.asList(NamedTimeAccelerator.values());
		acceleratorRoll.setItems(javafx.collections.FXCollections.observableList(listAccelerator));
		accelerator.bind(Bindings.select(acceleratorRoll.valueProperty(), "accelerator" ));

		acceleratorRoll.disableProperty().bind(timeAnimator.getRunning());

		try(InputStream fontStream = getClass()
				.getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf");){

			Font fontAwesome = Font.loadFont(fontStream, 15);	
			Button playPauseButton = new Button(PLAY_STRING);
			playPauseButton.setFont(fontAwesome);
			playPauseButton.setOnMouseClicked(e-> {

				if (play) {
					unBindAllDateTimeBean();
					timeAnimator.start();
					play = false;
					playPauseButton.setText(PAUSE_STRING);

				} else {
					bindAllDateTimeBean();
					timeAnimator.stop();
					playPauseButton.setText(PLAY_STRING);
					play = true;
				}
			});

			Button resetButton = new Button(RESET_STRING);
			resetButton.setFont(fontAwesome);		

			resetButton.setOnMouseClicked(e ->{	
				resetButtonProcess();				
			});	

			starNamesRoll = new ComboBox<String>();

			starNamesRoll.valueProperty().set("Rigel");
			java.util.Collections.sort(listStars);
			starNamesRoll.getItems().addAll(listStars);
			starNamesRoll.setStyle(" -fx-pref-width: 140;");

			starBox = Bindings.createObjectBinding(() ->  starNamesRoll.valueProperty().get(), starNamesRoll.valueProperty());
			canvasManager.getSelectedStarProperty().bind(starBox);

			Button tracePathButton = new Button(PEN_IDLE_STRING);
			tracePathButton.setFont(fontAwesome);

			tracePathButton.setOnMouseClicked(e -> {
				canvasManager.setTracePath(!trace);
				trace = !trace;

				if (trace) {
					tracePathButton.setText(PEN_ACTIVE_STRING);
				}	

				else {
					tracePathButton.setText(PEN_IDLE_STRING);
				}			
			});

			Button eraserButton = new Button(ERASER_STRING);
			eraserButton.setFont(fontAwesome);

			eraserButton.setOnMouseClicked(e -> {
				canvasManager.setClearPath(true);
				canvasManager.setClearPath(false);
			});

			Button parametersButton = new Button(COG_STRING);
			parametersButton.setFont(fontAwesome);

			CheckBox showAsterisms = tickBoxMaker("Show Asterims", fontAwesome, true, canvasManager.showAsterismsProperty());

			CheckBox showHorizon = tickBoxMaker("Show Horizon", fontAwesome, true, canvasManager.showHorizonProperty());

			CheckBox showStars = tickBoxMaker("Show Stars", fontAwesome, true, canvasManager.showStarsProperty());

			CheckBox showGrid = tickBoxMaker("Show Horizontal Coordinates Grid", fontAwesome, false, canvasManager.showGridProperty());

			CheckBox showEquator = tickBoxMaker("Show Equator", fontAwesome, true, canvasManager.showEquatorProperty());

			CheckBox showEcliptic = tickBoxMaker("Show Ecliptic", fontAwesome, true, canvasManager.showEclipticProperty());

			parametersButton.setOnMouseClicked(e -> {

				skyRoot.setEffect(new SepiaTone());
				VBox parametersRoot = new VBox();
				parametersRoot.setLayoutY(canvasManager.canvas().getHeight()/2 - 50);
				parametersRoot.setStyle("-fx-background-color: LightGray; -fx-alignment: center-left; -fx-spacing: 7.5; -fx-padding : 20px 20px 20px 20px;");

				Label parameters = new Label("Parameters");
				parameters.setFont(fontAwesome);

				Button confirm = new Button("Confirm");
				confirm.setFont(fontAwesome);

				parametersRoot.getChildren().addAll(parameters, showAsterisms, showHorizon, showStars, showGrid, showEquator, showEcliptic, confirm);

				Stage parametersStage = new Stage(StageStyle.TRANSPARENT);
				parametersStage.initOwner(primaryStage);
				parametersStage.initModality(Modality.APPLICATION_MODAL);
				parametersStage.setScene(new Scene(parametersRoot, Color.TRANSPARENT));

				confirm.setOnMouseClicked(event -> {
					skyRoot.setEffect(null);
					parametersStage.hide();
				});

				parametersStage.show();
			});

			resetButton.disableProperty().bind(timeAnimator.getRunning());

			Separator separator = new Separator();
			separator.setOrientation(Orientation.VERTICAL);

			Separator separatorSec = new Separator();
			separatorSec.setOrientation(Orientation.VERTICAL);

			timeBox.getChildren().addAll(acceleratorRoll, resetButton, playPauseButton, separator, starNamesRoll, tracePathButton, eraserButton, separatorSec, parametersButton);

			fontStream.close();		

		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}		

		return timeBox;
	}

	/**
	 * Instantiates a CheckBox corresponding the parameter described by its label (String), and its property in the class skyCanvasManager
	 * 
	 * @param label
	 * 			Label of the parameter
	 * 
	 * @param font
	 * 			Font of the CheckBox
	 * 
	 * @param initValue
	 * 			Defines if the object is drawn at the start of the application
	 * 
	 * @param selectedProperty
	 * 			Property of the parameter in skyCanvasManager
	 * 
	 * @return CheckBox corresponding the parameter
	 */
	private CheckBox tickBoxMaker(String label, Font font, boolean initValue, BooleanProperty selectedProperty) {
		CheckBox showProperty = new CheckBox(label);
		showProperty.setFont(font);
		showProperty.setSelected(initValue);
		selectedProperty.bind(showProperty.selectedProperty());

		return showProperty;
	}

	/**
	 * Reset process consists of unbinding date time properties setting the start values and binding again
	 */	
	private void resetButtonProcess() {
		unBindAllDateTimeBean();
		zoneIdRoll.valueProperty().set(when.getZone().toString());
		dateTimeBean.setZonedDateTime(when);
		bindAllDateTimeBean();
	}

	/**
	 * Binds all the date time bean properties respectively to the time formatter, date picker and boxZoneId
	 */
	private void bindAllDateTimeBean() {
		dateTimeBean.timeProperty().bindBidirectional(timeFormatter.valueProperty());
		dateTimeBean.dateProperty().bindBidirectional(datePicker.valueProperty());
		dateTimeBean.zoneProperty().bind(boxZoneId);
	}

	/**
	 * Unbinds all the date time bean properties
	 */
	private void unBindAllDateTimeBean() {
		dateTimeBean.timeProperty().unbind();
		dateTimeBean.dateProperty().unbind();
		dateTimeBean.zoneProperty().unbind();
	}

	/**
	 * Instantiate the BorderPane corresponding to the information bar, containing the information the FOV, closest celestial object to the mouse and mouse position
	 * 
	 * @return the BorderPane corresponding to the information bar, 
	 */
	private BorderPane infoBar() {
		Text fieldOfViewText = new Text();
		fieldOfViewText.textProperty().setValue("Champ de vue: 100°");
		fieldOfViewText.textProperty().bind(Bindings.format("Champ de vue : %.1f°",
				viewingParametersBean.fieldOfViewDegProperty()));

		objectUnderMouseString = Bindings.createStringBinding(
				() -> {
					try {	
						return canvasManager.objectUnderMouseProperty().getValue().info();

					} catch (NullPointerException e) {	
						return null;
					} 

				}, canvasManager.objectUnderMouseProperty());

		Text closestObjectText = new Text();
		closestObjectText.textProperty().bind(objectUnderMouseString);

		Text mousePosition = new Text();
		mousePosition.textProperty().bind(Bindings.format("Azimut : %.1f°, hauteur : %.1f°", 
				canvasManager.mouseAzDegProperty(), 
				canvasManager.mouseAltDegProperty()));

		BorderPane informationBar = new BorderPane(closestObjectText, null,mousePosition ,null, fieldOfViewText );	
		informationBar.setStyle("-fx-padding: 4; -fx-background-color: white;");

		return informationBar;
	}
}