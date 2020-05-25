package ch.epfl.rigel.astronomy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;

import javax.imageio.ImageIO;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.gui.SkyCanvasPainter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public final class DrawSky extends Application {
	  public static void main(String[] args) { launch(args); }

	  private InputStream resourceStream(String resourceName) {
	    return getClass().getResourceAsStream(resourceName);
	  }

	  @Override
	  public void start(Stage primaryStage) throws Exception {
	    try (InputStream hs = resourceStream("/hygdata_v3.csv");
	    	InputStream as = resourceStream("/asterisms.txt")){
	      StarCatalogue catalogue = new StarCatalogue.Builder()
		.loadFrom(hs, HygDatabaseLoader.INSTANCE)
		.loadFrom(as, AsterismLoader.INSTANCE)
		.build();

	      ZonedDateTime when =
		ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
	      GeographicCoordinates where =
		GeographicCoordinates.ofDeg(6.57, 46.52);
	      HorizontalCoordinates projCenter =
	     //HorizontalCoordinates.ofDeg(0, 90);
	      HorizontalCoordinates.ofDeg(3.7, -65);
		//HorizontalCoordinates.ofDeg(180, 45);
	      StereographicProjection projection =
		new StereographicProjection(projCenter);
	      ObservedSky sky =
		new ObservedSky(when, where, projection, catalogue);

	      Canvas canvas =
		new Canvas(800, 600);
	      Transform planeToCanvas =
	     //Transform.affine(260, 0, 0, -260, 400, 300);
		Transform.affine(1300, 0, 0, -1300, 400, 300);
	      SkyCanvasPainter painter =
		new SkyCanvasPainter(canvas);

	      painter.drawSky(sky, projection, planeToCanvas, Color.BLACK);
	      //painter.drawSun(sky, projection, planeToCanvas);

	      WritableImage fxImage =
		canvas.snapshot(null, null);
	      BufferedImage swingImage =
		SwingFXUtils.fromFXImage(fxImage, null);
	      ImageIO.write(swingImage, "png", new File("sky.png"));
	    }
	    Platform.exit();
	  }
	}
