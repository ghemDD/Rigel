package ch.epfl.rigel.gui;
import static java.time.Duration.*;
import static ch.epfl.rigel.gui.TimeAccelerator.*;

/**
 * Enumeration of various accelerators
 * 
 * @author Nael Ouerghemi (310435)
 */
public enum NamedTimeAccelerator {

	TIMES_1("1×", continuous(1)),
	TIMES_30("30×", continuous(30)),
	TIMES_300("300×", continuous(300)),
	TIMES_3000("3000×", continuous(3000)),
	DAY("jour", discrete(60, ofHours(24))),
	SIDEREAL_DAY("jour sidéral", discrete(60, ofHours(23)
			.plusMinutes(56)
			.plusSeconds(4)));

	private final String name;
	private final TimeAccelerator accelerator;

	private NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
		this.name = name;
		this.accelerator = accelerator;
	}

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
