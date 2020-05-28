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

	private final ObjectProperty<LocalDate> date;
	private final ObjectProperty<LocalTime> time;
	private final ObjectProperty<ZoneId> zone;
	private final ObjectBinding<ZonedDateTime> zonedDateTime;


	/**
	 * Constructor for date time bean : initialises date, time, zone properties and zonedDateTime binding
	 */
	public DateTimeBean() {
		date = new SimpleObjectProperty<LocalDate>();
		time = new SimpleObjectProperty<LocalTime>();
		zone = new SimpleObjectProperty<ZoneId>();
		zonedDateTime = Bindings.createObjectBinding(
				() -> ZonedDateTime.of(date.get(), 
						time.get(), zone.get()), date, time, zone) ;
	}

	//TIME PROPERTY
	/**
	 * Getter for the time property
	 * 
	 * @return time property
	 */
	public ObjectProperty<LocalTime> timeProperty() {return time;}

	/**
	 * Getter for the value of time property
	 * 
	 * @return value LocalTime of time property
	 */
	public LocalTime getTime() {return time.get();}

	/**
	 * Setter for time property
	 * 
	 * @param time
	 * 			Value to be set in
	 * 
	 */
	public void setTime(LocalTime timeParameter) {time.set(timeParameter);}


	//DATE PROPERTY
	/**
	 * Getter for date property
	 * 
	 * @return date property
	 */
	public ObjectProperty<LocalDate> dateProperty() {return date;}

	/**
	 * Getter for the value of date property
	 * 
	 * @return value LocalDate of date property
	 */
	public LocalDate getDate() {return date.get();}

	/**
	 * Setter for date property
	 * 
	 * @param date
	 * 			Value to be set in
	 */
	public void setDate(LocalDate dateParameter) {date.set(dateParameter);}

	//ZONED DATE TIME PROPERTY
	/**
	 * Getter for the value of zonedDateTime property
	 * 
	 * @return value of zonedDateTime property
	 */
	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime.get();
	}

	/**
	 * Setter for zonedDateTimeProperty
	 * 
	 * @param zdtParam
	 * 			Value to be set in
	 */
	public void setZonedDateTime(ZonedDateTime zdtParam) {
		date.set(zdtParam.toLocalDate());
		time.set(LocalTime.of(zdtParam.getHour(),
				zdtParam.getMinute(), 
				zdtParam.getSecond(), 
				zdtParam.getNano()));
		zone.set(zdtParam.getZone());
	}

	/**
	 * Getter for zonedDateTimeProperty
	 * 
	 * @return zonedDateTime property
	 */
	public ObjectBinding<ZonedDateTime> getZonedDateTimeProperty() {return zonedDateTime;}


	//ZONE PROPERTY
	/**
	 * Getter for zone property
	 * 
	 * @return zone property
	 */
	public ObjectProperty<ZoneId> zoneProperty(){return zone;}

	/**
	 * Getter for the value of zone property
	 * 
	 * @return value of zone property
	 */
	public ZoneId getZone() {return zone.get();}

	/**
	 * Setter for zone property
	 * 
	 * @param zone
	 * 			Value to be set in
	 */
	public void setZone(ZoneId zoneParameter) {zone.set(zoneParameter);}
}
