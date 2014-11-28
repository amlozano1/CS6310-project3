package base;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testToSimulationTime() {
//		Calendar c = (Calendar)Utils.START_TIME.clone();
//		c.add(Calendar.MINUTE, 1440);
//		
//		long minutesElapsed = Utils.toSimulationTime(c.getTime());
//		assertEquals(1440, minutesElapsed);
	
		Date d = new Date(1388811600000l);
		long minutesElapsed = Utils.toSimulationTime(d);
	}
	
	@Test
	public void testToGMT(){
		Date d = new Date(1388811600000l);
		System.out.println(d);
		System.out.println(d.getTimezoneOffset());
		System.out.println(d.getTimezoneOffset() * 60 * 1000);
		System.out.println(TimeZone.getDefault().getRawOffset());
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.setTime(d);
		
		System.out.println(Utils.START_TIME);
		System.out.println(Utils.START_TIME.getTime());

	}
	

}
