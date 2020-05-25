package ch.epfl.rigel.gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Bean containing the time information
 * 
 * @author Nael Ouerghemi (310435)
 */
public final class DateTimeBean {

	private final ObjectProperty<LocalDate> dateProperty;
	private final ObjectProperty<LocalTime> timeProperty;
	private final ObjectProperty<ZoneId> zoneProperty;
	private final ObjectBinding<ZonedDateTime> zdtProperty;

	public DateTimeBean() {
		dateProperty = new SimpleObjectProperty<LocalDate>();
		timeProperty = new SimpleObjectProperty<LocalTime>();
		zoneProperty = new SimpleObjectProperty<ZoneId>();
		zdtProperty = Bindings.createObjectBinding(() -> ZonedDateTime.of(dateProperty.get(), timeProperty.get(), zoneProperty.get()), dateProperty, timeProperty, zoneProperty) ;
	}

	//TIME PROPERTY
	/**
	 * Getter for the time property
	 * 
	 * @return time property
	 */
	public ObjectProperty<LocalTime> timeProperty() {return timeProperty;}

	/**
	 * Getter for the value of time property
	 * 
	 * @return value LocalTime of time property
	 */
	public LocalTime getTime() {return timeProperty.get();}

	/**
	 * Setter for time property
	 * 
	 * @param time
	 * 			Value to be set in
	 * 
	 */
	public void setTime(LocalTime timeParameter) {timeProperty.set(timeParameter);}


	//DATE PROPERTY
	/**
	 * Getter for date property
	 * 
	 * @return date property
	 */
	public ObjectProperty<LocalDate> dateProperty(){return dateProperty;}

	/**
	 * Getter for the value of date property
	 * 
	 * @return value LocalDate of date property
	 */
	public LocalDate getDate() {return dateProperty.get();}

	/**
	 * Setter for date property
	 * 
	 * @param date
	 * 			Value to be set in
	 */
	public void setDate(LocalDate dateParameter) {dateProperty.set(dateParameter);}

	//ZONED DATE TIME PROPERTY
	/**
	 * Getter for the value of zonedDateTime property
	 * 
	 * @return value of zonedDateTime property
	 */
	public ZonedDateTime getZonedDateTime() {
		return zdtProperty.get();
	}

	/**
	 * Setter for zonedDateTimeProperty
	 * 
	 * @param zdtParam
	 * 			Value to be set in
	 * 
	 */
	public void setZonedDateTime(ZonedDateTime zdtParam) {
		dateProperty.set(zdtParam.toLocalDate());
		timeProperty.set(LocalTime.of(zdtParam.getHour(),
				zdtParam.getMinute(), 
				zdtParam.getSecond(), 
				zdtParam.getNano()));
		zoneProperty.set(zdtParam.getZone());
	}

	/**
	 * Getter for zonedDateTimeProperty
	 * 
	 * @return zonedDateTime property
	 */
	public ObjectBinding<ZonedDateTime> getZonedDateTimeProperty() {return zdtProperty;}


	//ZONE PROPERTY
	/**
	 * Getter for zone property
	 * 
	 * @return zone property
	 */
	public ObjectProperty<ZoneId> zoneProperty(){return zoneProperty;}

	/**
	 * Getter for the value of zone property
	 * 
	 * @return value of zone property
	 */
	public ZoneId getZone() {return zoneProperty.get();}

	/**
	 * Setter for zone property
	 * 
	 * @param zone
	 * 			Value to be set in
	 */
	public void setZone(ZoneId zoneParameter) {zoneProperty.set(zoneParameter);}
}
