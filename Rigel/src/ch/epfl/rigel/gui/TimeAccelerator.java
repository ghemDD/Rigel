package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Represents a time accelerator 
 * 
 * @author Tanguy Marbot (316756)
 */

@FunctionalInterface
public interface TimeAccelerator {
	/**
	 * Simulates an time acceleration given an actual date and the elapsed real time
	 *
	 * @param initialTime
	 *          DatedTime at the beginning of the simulation
	 *          
	 * @param realTime
	 *          Real time elapsed from the beginning of the animation (in nanoseconds)
	 *
	 * @return accelerated time (in ZonedDateTime format)
	 */
	public abstract ZonedDateTime adjust(ZonedDateTime initialTime, long realTime);

	/**
	 * Simulates a continuous time accelerator given a speedFactor
	 *
	 * @param speedFactor
	 *          Speed coefficient by which the time is sped up to
	 *          
	 * @return continuous time accelerator given a speedFactor
	 */
	public static TimeAccelerator continuous(int factor) {
		return (initialTime, realTime) -> initialTime.plusNanos(factor * realTime);
	}

	/**
	 * Simulate a discrete time accelerator given a frequency and a step of simulation
	 *
	 * @param frequency
	 *          Frequency of the simulation (in Hz)
	 *          
	 * @param step
	 *          Discrete time period of the simulated time (in Duration format)
	 *          
	 * @return a discrete time accelerator given a frequency and a step of simulation
	 */
	public static TimeAccelerator discrete(double frequency, Duration step) {
		return (initialTime, realTime) -> initialTime.plusNanos( ((long) Math.floor(frequency * realTime/1e9)) * step.toNanos());
	}
}
