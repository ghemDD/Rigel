package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ObservedSkyTest {
    private final static String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";
    private final static String ASTERISM_CATALOGUE_NAME = "/asterisms.txt";

    private static final ZonedDateTime ZDT_SEMESTER_START = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(13, 15),
            ZoneOffset.ofHours(1));

    private static final ZonedDateTime ZDT_FRAMAPAD = ZonedDateTime.of(
            LocalDate.of(2020, Month.APRIL, 4),
            LocalTime.of(0, 0),
            ZoneOffset.UTC);

    @Test
    void planetPositionsAreCalculatedProperly() throws IOException{
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            var geoCoords = GeographicCoordinates.ofDeg(30, 45);
            var stereographic = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
            var equToHor = new EquatorialToHorizontalConversion(ZDT_FRAMAPAD, geoCoords);
            var observedSky = new ObservedSky(ZDT_FRAMAPAD, geoCoords, stereographic, test);

            int i=0;
            for(Planet p : observedSky.planets()){
                var horCoords = equToHor.apply(p.equatorialPos());
                var cartCoords = stereographic.apply(horCoords);

                double expectedX = cartCoords.x();
                double expectedY = cartCoords.y();
                assertEquals(expectedX, observedSky.planetPositions()[i]);
                assertEquals(expectedY, observedSky.planetPositions()[i+1]);
                i+=2;
            }
        }
    }

    @Test
    void starPositionsAreCalculatedProperly() throws IOException{
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            var geoCoords = GeographicCoordinates.ofDeg(30, 45);
            var stereographic = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
            var equToHor = new EquatorialToHorizontalConversion(ZDT_FRAMAPAD, geoCoords);
            var observedSky = new ObservedSky(ZDT_FRAMAPAD, geoCoords, stereographic, test);

            int i=0;
            for(Star s : observedSky.stars()){
                var horCoords = equToHor.apply(s.equatorialPos());
                var cartCoords = stereographic.apply(horCoords);

                double expectedX = cartCoords.x();
                double expectedY = cartCoords.y();
                assertEquals(expectedX, observedSky.starPositions()[i]);
                assertEquals(expectedY, observedSky.starPositions()[i+1]);
                i+=2;
            }
        }
    }

    @Test
    void OCTWorksWithFramapadValues() throws IOException{
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            var geoCoords = GeographicCoordinates.ofDeg(30, 45);
            var stereographic = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
            var equToHor = new EquatorialToHorizontalConversion(ZDT_FRAMAPAD, geoCoords);
            var observedSky = new ObservedSky(ZDT_FRAMAPAD, geoCoords, stereographic, test);

            var equCoords = EquatorialCoordinates.of(0.004696959812148989,-0.861893035343076);
            var horCoords = equToHor.apply(equCoords);

            var cartesian = stereographic.apply(horCoords);
            System.out.println(cartesian.toString());

            var result1 = observedSky.objectClosestTo(cartesian, 30);
            var result2 = observedSky.objectClosestTo(cartesian, 0.001);
            //assertEquals("Tau Phe", result1.get().name());
            assertEquals(Optional.empty(), result2);
        }
    }

    @Test
    void OCTWorksWithRigel() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            var geoCoords = GeographicCoordinates.ofDeg(0, 0); //TO CHANGE
            var stereographic = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 0)); //TO CHANGE
            var equToHor = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, geoCoords);
            var observedSky = new ObservedSky(ZDT_SEMESTER_START, geoCoords, stereographic, test);

            var equCoords = EquatorialCoordinates.of(1.3724303693276385, -0.143145630755865); //RIGEL
            var horCoords = equToHor.apply(equCoords);
            var cartesian = stereographic.apply(horCoords);
            System.out.println(cartesian.toString());
            double maxDistance = 100; //TO CHANGE
            
            Optional<CelestialObject> o = observedSky.objectClosestTo(cartesian, maxDistance);
            assertEquals("Rigel", o.get().name());
        }
    }

    @Test
    void OCTWorksWithBetelgeuse() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            var geoCoords = GeographicCoordinates.ofDeg(0, 0); //TO CHANGE
            var stereographic = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 0)); //TO CHANGE
            var equToHor = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, geoCoords);
            var observedSky = new ObservedSky(ZDT_SEMESTER_START, geoCoords, stereographic, test);

            var equCoords = EquatorialCoordinates.of(1.5497291183713153, 0.12927763169419373); //BETELGEUSE
            var horCoords = equToHor.apply(equCoords);
            var cartesian = stereographic.apply(horCoords);
            System.out.println(cartesian.toString());
            double maxDistance = 100; //TO CHANGE

            Optional<CelestialObject> o = observedSky.objectClosestTo(cartesian, maxDistance);
            assertEquals("Betelgeuse", o.get().name());
        }
    }
}
