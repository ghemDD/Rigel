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
 * @author Tanguy Marbot (316756)
 */
public final class TimeAnimator extends AnimationTimer {

	//Properties
	private final DateTimeBean dateInstant;
	private final SimpleObjectProperty<TimeAccelerator> accelerator;
	private final SimpleBooleanProperty running;

	//Initial conditions inn method handle()
	private boolean first;
	private long firstTimePassed;
	private ZonedDateTime dateFirst;

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
		accelerator = new SimpleObjectProperty<TimeAccelerator>();
		dateFirst = null;
	}

	/**
	 * @see AnimationTimer#handle()
	 */
	@Override
	public void handle(long now) {

		if (first) {
			firstTimePassed = now;
			first = false;
			dateFirst = dateInstant.getZonedDateTime();
		}

		long delta = now - firstTimePassed;

		ZonedDateTime adjustedDate = accelerator.get().adjust(dateFirst, delta);
		dateInstant.setZonedDateTime(adjustedDate);
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
	 * @param accelerator
	 * 			Accelerator to be set in
	 * 
	 */
	public void setAccelerator(TimeAccelerator accel) {
		accelerator.set(accel);
	}

	/**
	 * Getter for the time accelerator
	 * 
	 * @return time accelerator
	 */
	public TimeAccelerator getAccelerator() {
		return accelerator.get();
	}

	/**
	 * Getter for the accelerator property
	 * 
	 * @return accelerator property
	 */
	public ObjectProperty<TimeAccelerator> getAcceleratorProperty() {
		return accelerator;
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
