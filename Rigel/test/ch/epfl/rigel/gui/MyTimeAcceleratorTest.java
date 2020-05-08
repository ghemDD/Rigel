package ch.epfl.rigel.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class MyTimeAcceleratorTest {

	
	@Test
	void timeContinuousTest() {
		ZonedDateTime initialTime = ZonedDateTime.parse("2020-04-17T21:00:00+00:00");
		ZonedDateTime laterTime = TimeAccelerator.continuous(300).adjust(initialTime, (long) (2.34 * 1e9));
		assertEquals(ZonedDateTime.parse("2020-04-17T21:11:42+00:00"), laterTime);
	}
	
	@Test
	void timeDiscreteTest() {
		ZonedDateTime initialTime = ZonedDateTime.parse("2020-04-20T21:00:00+00:00");
	      ZonedDateTime laterTime = TimeAccelerator.discrete(10, Duration.parse("PT23H56M4S"))
	.adjust(initialTime,(long) (2.34 * 1e9));
	 
	 long realTime = (long) (2.34 * 1e9);
	 //System.out.println(realTime);
	 //System.out.println("Floor "+ ((long) Math.floor(10 * realTime/1e9)));
	 //System.out.println("Nano "+ (((long) Math.floor(10 * realTime/1e9))  * Duration.parse("PT23H56M4S").toNanos()));
	 //System.out.println(initialTime.plusNanos((long) (24 * 3600 * 1e9)));
	 
	assertEquals(ZonedDateTime.parse("2020-05-13T19:29:32+00:00"), laterTime);
	}
}
