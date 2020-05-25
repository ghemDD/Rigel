package ch.epfl.rigel.gui;


import java.time.ZonedDateTime;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Represents a time animator
 * 
 * @author Nael Ouerghemi (310435)
 *
 */
public final class TimeAnimator extends AnimationTimer {

	private final DateTimeBean dateInstant;
	private final SimpleObjectProperty<TimeAccelerator> acceleratorProperty;
	private final SimpleBooleanProperty running;
	private boolean first;
	private long lastTimePassed;

	/**
	 * Constructor for time animator
	 * 
	 * @param dateInstant
	 * 			DateTimeBean equivalent to the instant of observation
	 * 
	 */
	public TimeAnimator(DateTimeBean dateInstant) {
		this.dateInstant  = dateInstant;
		running = new SimpleBooleanProperty();
		acceleratorProperty = new SimpleObjectProperty<TimeAccelerator>();
	}

	/**
	 * @see AnimationTimer#handle()
	 */
	@Override
	public void handle(long now) {

		if (first) {
			lastTimePassed = now;
			first = false;
		}

		long delta = now - lastTimePassed;

		ZonedDateTime date = dateInstant.getZonedDateTime();
		ZonedDateTime adjustedDate = acceleratorProperty.get().adjust(date, delta);
		dateInstant.setZonedDateTime(adjustedDate);
		lastTimePassed = now;
	}

	/**
	 * @see AnimationTimer#start()
	 */
	@Override
	public void start() {
		super.start();
		first = true;
		running.set(true);
	}

	/**
	 * @see AnimationTimer#stop()
	 */
	@Override
	public void stop() {
		super.stop();
		running.set(false);
	}

	/**
	 * Setter for the time accelerator
	 * 
	 * @param accel
	 * 			Accelerator to be set in
	 * 
	 */
	public void setAccelerator(TimeAccelerator accel) {
		acceleratorProperty.set(accel);
	}

	/**
	 * Getter for the time accelerator
	 * 
	 * @return time accelerator
	 */
	public TimeAccelerator getAccelerator() {
		return acceleratorProperty.get();
	}

	/**
	 * Getter for the accelerator property
	 * 
	 * @return accelerator property
	 */
	public ObjectProperty<TimeAccelerator> getAcceleratorProperty() {
		return acceleratorProperty;
	}

	/**
	 * Returns running property in the form of ReadOnlyBooleanProperty
	 * Not external modification possible
	 * 
	 * @return running property in the form of ReadOnlyBooleanProperty
	 */
	public ReadOnlyBooleanProperty getRunning() {
		return running;
	}
}
