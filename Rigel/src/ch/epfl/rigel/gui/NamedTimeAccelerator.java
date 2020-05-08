package ch.epfl.rigel.gui;
import static java.time.Duration.ofSeconds;

public enum NamedTimeAccelerator {

	TIMES_1("1×", TimeAccelerator.continuous(1)),
	TIMES_30("30×", TimeAccelerator.continuous(30)),
	TIMES_300("300×", TimeAccelerator.continuous(300)),
	TIMES_3000("3000×", TimeAccelerator.continuous(3000)),
	DAY("jour", TimeAccelerator.discrete(1, ofSeconds(32))),
	SIDEREAL_DAY("jour sidéral", TimeAccelerator.discrete(1, ofSeconds(30)));

	private NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
		this.name = name;
		this.accelerator = accelerator;
	}

	private String name;
	private TimeAccelerator accelerator;

	/**
	 *	Getter for the name of the pair
	 * 
	 * @return name of the pair
	 */
	public String getName() {return name;}

	/**
	 * Getter for the accelerator of the pair
	 * 
	 * @return accelerator of the pair
	 */
	public TimeAccelerator getAccelerator() {return accelerator;}

	@Override
	/**
	 * @see Object#toString()
	 */
	public String toString() {return name;}
}
