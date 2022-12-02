package de.nrw.schule.svws.davapi.util.iCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.davapi.util.icalendar.DateTimeUtil;
import de.nrw.schule.svws.davapi.util.icalendar.PropertyKeys;
import de.nrw.schule.svws.davapi.util.icalendar.VCalendar;
import de.nrw.schule.svws.davapi.util.icalendar.VEvent;
import de.nrw.schule.svws.davapi.util.icalendar.VTimezone;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.ByDay;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.DateListProperty;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.Frequency;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.RRule;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.RecurrencyLimit;
import de.nrw.schule.svws.davapi.util.icalendar.recurrence.WeekDay;

/**
 * Diese Klasse testet die Serialisierung von Kalenderereignissen und zeigt
 * Beispiele für die Erstellung von neuen Kalenderereignissen.
 */
public class VCalendarSerialisationTest {

	private static final String expectedVCalendar1 = """
			BEGIN:VCALENDAR\r
			VERSION:2.0\r
			BEGIN:VTIMEZONE\r
			TZID:Europe/Berlin\r
			BEGIN:DAYLIGHT\r
			TZOFFSETFROM:+0100\r
			TZOFFSETTO:+0200\r
			TZNAME:CEST\r
			DTSTART:19700329T020000\r
			RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r
			END:DAYLIGHT\r
			BEGIN:STANDARD\r
			TZOFFSETFROM:+0200\r
			TZOFFSETTO:+0100\r
			TZNAME:CET\r
			DTSTART:19701025T030000\r
			RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r
			END:STANDARD\r
			END:VTIMEZONE\r
			BEGIN:VEVENT\r
			DTSTART;TZID=Europe/Berlin:20220829T080000\r
			DTEND;TZID=Europe/Berlin:20220829T084500\r
			DESC:Die Beschreibung des Events\r
			SUMMARY:Erste Unterrichtsstunde\r
			RRULE:FREQ=WEEKLY\r
			END:VEVENT\r
			END:VCALENDAR\r
						""";

	private String expectedVCalendar2 = 
			"""
BEGIN:VCALENDAR\r
VERSION:2.0\r
BEGIN:VTIMEZONE\r
TZID:Europe/Berlin\r
BEGIN:DAYLIGHT\r
TZOFFSETFROM:+0100\r
TZOFFSETTO:+0200\r
TZNAME:CEST\r
DTSTART:19700329T020000\r
RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r
END:DAYLIGHT\r
BEGIN:STANDARD\r
TZOFFSETFROM:+0200\r
TZOFFSETTO:+0100\r
TZNAME:CET\r
DTSTART:19701025T030000\r
RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r
END:STANDARD\r
END:VTIMEZONE\r
BEGIN:VEVENT\r
DTSTART;TZID=Europe/Berlin:20220829T080000\r
DTEND;TZID=Europe/Berlin:20220829T084500\r
DESC:Die Beschreibung des Events\r
SUMMARY:Erste Unterrichtsstunde\r
EXDATE;TIZD=Europe/Berlin;VALUE=DATE:20221224,20221225,20221226,20221227,20221228,20221229,20221230,20221231,20230101,20230102,20230103,20230104,20230105,20230106\r
RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=20230622T220000Z;BYDAY=MO,FR\r
END:VEVENT\r
END:VCALENDAR\r
""";

	/**
	 * testet die Serialisierung beim Erstellen des Events mit
	 * {@link VEvent#createSimpleEvent(Instant, Instant, String, String)}
	 */
	@Test
	void serializeSimpleEventTest() {
		VCalendar calendar = new VCalendar();
		// vordefinierte Zeitzone für Deutschland incl. Winter-/Sommerzeit Definition
		calendar.setTimezoneDefinition(VTimezone.DEFAULT_TZ_BERLIN_EUROPE);
		// das Event erhält die eigentlichen Daten für das Ereignis
		// startzeit als Instant - hier in UTC
		Instant start = Instant.parse("2022-08-29T06:00:00.000Z");
		// startzeit als Instant - hier in GMT über unsere Hilfsmethoden
		Instant end = DateTimeUtil.fromSqlTimeStamp("2022-08-29T08:45:00.000");
		VEvent event = VEvent.createSimpleEvent(start, end, "Erste Unterrichtsstunde", "Die Beschreibung des Events");
		// Regel für wöchentliche Wiederholungen
		RRule r = new RRule(Frequency.WEEKLY);
		event.addProperty(r);
		calendar.getEvents().add(event);
		String serialized = calendar.serialize();
		assertEquals(expectedVCalendar1, serialized);
	}

	/**
	 * testet die Serialisierung beim Erstellen des Events mit
	 * {@link VEvent#createSimpleEvent(Instant, Instant, String, String)}
	 */
	@Test
	void serializeSimpleEventWithExDatesTest() {
		VCalendar calendar = new VCalendar();
		// vordefinierte Zeitzone für Deutschland incl. Winter-/Sommerzeit Definition
		calendar.setTimezoneDefinition(VTimezone.DEFAULT_TZ_BERLIN_EUROPE);
		// das Event erhält die eigentlichen Daten für das Ereignis
		// startzeit als Instant - hier in UTC
		Instant start = Instant.parse("2022-08-29T06:00:00.000Z");
		// startzeit als Instant - hier in GMT über unsere Hilfsmethoden
		Instant end = DateTimeUtil.fromSqlTimeStamp("2022-08-29T08:45:00.000");
		VEvent event = VEvent.createSimpleEvent(start, end, "Erste Unterrichtsstunde", "Die Beschreibung des Events");
		// Regel für 2wöchentliche Wiederholungen an Montagen und Freitagen
		RRule r = new RRule(Frequency.WEEKLY);
		r.setInterval(2);
		r.getByDays().add(new ByDay(WeekDay.MONDAY));
		r.getByDays().add(new ByDay(WeekDay.FRIDAY));
		Instant schuljahresende = LocalDate.of(2023, 6, 23).atStartOfDay(ZoneId.of(DateTimeUtil.TIMEZONE_DEFAULT))
				.toInstant();
		r.setLimit(new RecurrencyLimit(schuljahresende));
		// Ausnahmedaten, bspw. für Ferien
		LocalDate ferienBeginn = LocalDate.of(2022, 12, 24);
		LocalDate ferienEnde = LocalDate.of(2023, 1, 7);
		DateListProperty exDates = new DateListProperty(PropertyKeys.EXDATE);
		exDates.addAllBetween(ferienBeginn, ferienEnde);
		event.addProperty(exDates);
		event.addProperty(r);
		calendar.getEvents().add(event);
		String serialized = calendar.serialize();
		assertEquals(expectedVCalendar2, serialized);
	}

}
