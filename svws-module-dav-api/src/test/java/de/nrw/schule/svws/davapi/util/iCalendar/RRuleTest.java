package de.nrw.schule.svws.davapi.util.iCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.davapi.util.icalendar.recurrence.ByDay;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.Frequency;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.RRule;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.RecurrencyLimit;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.WeekDay;

/**
 * Testet das Erstellen, Serialisieren und Deserialisieren von RRules
 * 
 */
class RRuleTest {

	/**
	 * Testet die Serialisierung von RRules anhand von Beispielen
	 */
	@Test
	void testSerialization() {
		RRule r = new RRule(Frequency.DAILY);
		r.setLimit(new RecurrencyLimit(10));
		String serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=DAILY;COUNT=10\r\n", serialized);

		r = new RRule(Frequency.WEEKLY);
		r.setInterval(2);
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=WEEKLY;INTERVAL=2\r\n", serialized);

		r = new RRule(Frequency.MONTHLY);
		r.setInterval(10);
		r.setLimit(new RecurrencyLimit(5));
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=MONTHLY;INTERVAL=10;COUNT=5\r\n", serialized);

		r = new RRule(Frequency.YEARLY);
		r.setLimit(new RecurrencyLimit(3));
		r.getByMonths().add(1);
		r.getByDays().add(new ByDay(WeekDay.MONDAY));
		r.getByDays().add(new ByDay(WeekDay.TUESDAY));
		r.getByDays().add(new ByDay(WeekDay.SUNDAY));
		r.getByDays().add(new ByDay(WeekDay.WEDNESDAY));
		r.getByDays().add(new ByDay(WeekDay.SATURDAY));
		r.getByDays().add(new ByDay(WeekDay.FRIDAY));
		r.getByDays().add(new ByDay(WeekDay.THURSDAY));
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=YEARLY;COUNT=3;BYDAY=SU,MO,TU,WE,TH,FR,SA;BYMONTH=1\r\n", serialized);

		r = new RRule(Frequency.DAILY);
		r.setLimit(new RecurrencyLimit(3));
		r.getByMonths().add(1);
		r.getByMonths().add(3);
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=DAILY;COUNT=3;BYMONTH=1,3\r\n", serialized);

		r = new RRule(Frequency.MONTHLY);
		r.setLimit(new RecurrencyLimit(6));
		r.getByDays().add(new ByDay(-2, WeekDay.MONDAY));
		r.getByDays().add(new ByDay(-1, WeekDay.MONDAY));
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO,-1MO\r\n", serialized);

		r = new RRule(Frequency.MONTHLY);
		r.setLimit(new RecurrencyLimit(3));
		r.getByDays().add(new ByDay(WeekDay.TUESDAY));
		r.getByDays().add(new ByDay(WeekDay.WEDNESDAY));
		r.getByDays().add(new ByDay(WeekDay.WEDNESDAY));
		r.getByDays().add(new ByDay(WeekDay.THURSDAY));
		r.getBySetPos().add(3);
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3\r\n", serialized);

		r = new RRule(Frequency.DAILY);
		r.setLimit(new RecurrencyLimit(Instant.parse("1997-12-24T00:00:00.00z")));
		serialized = getSerializedRRule(r);
		assertEquals("RRULE:FREQ=DAILY;UNTIL=19971224T000000Z\r\n", serialized);
	}

	/**
	 * Testet die deserialisierung von Recurrence Rules
	 */
	@Test
	void testDeserialisation() {
		String s = "RRULE:FREQ=DAILY;COUNT=10\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=WEEKLY;INTERVAL=2\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=MONTHLY;INTERVAL=10;COUNT=5\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=YEARLY;COUNT=3;BYDAY=SU,MO,TU,WE,TH,FR,SA;BYMONTH=1\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=DAILY;COUNT=3;BYMONTH=1,3\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO,-1MO\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));

		s = "RRULE:FREQ=DAILY;UNTIL=19971224T000000Z\r\n";
		assertEquals(s, getSerializedRRule(RRule.fromString(s)));
	}

	/**
	 * Serialisiert eine gegebene RRule zu einem String
	 * 
	 * @param r die RRUle
	 * @return die Stringrepräsentation der Regel
	 */
	private static String getSerializedRRule(RRule r) {
		StringBuffer sb = new StringBuffer();
		r.serialize(sb);
		return sb.toString();
	}

	/**
	 * Testet die Serialisierung und De-Serialisierung von {@link ByDay}
	 */
	@Test
	void testByDaySerialisation() {
		String s = "-1MO";
		ByDay fromString = ByDay.fromString(s);
		assertEquals(-1, fromString.getOrdinal());
		assertEquals(WeekDay.MONDAY, fromString.getWeekDay());
		assertEquals(s, fromString.toString());

		s = "200TU";
		fromString = ByDay.fromString(s);
		assertEquals(200, fromString.getOrdinal());
		assertEquals(WeekDay.TUESDAY, fromString.getWeekDay());
		assertEquals(s, fromString.toString());

		s = "WE";
		fromString = ByDay.fromString(s);
		assertEquals(0, fromString.getOrdinal());
		assertEquals(WeekDay.WEDNESDAY, fromString.getWeekDay());
		assertEquals(s, fromString.toString());

		assertThrows(IllegalArgumentException.class, () -> ByDay.fromString("ED"));
		assertThrows(IndexOutOfBoundsException.class, () -> ByDay.fromString("W"));
		assertThrows(IllegalArgumentException.class, () -> ByDay.fromString("WED"));
		assertThrows(IllegalArgumentException.class, () -> ByDay.fromString("W123WE"));
	}

}
