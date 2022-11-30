package de.nrw.schule.svws.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.davapi.util.icalendar.IProperty;
import de.nrw.schule.svws.davapi.util.icalendar.VCalendar;
import de.nrw.schule.svws.davapi.util.icalendar.VCalendarTyp;

/**
 * Testklasse für {@link VCalendar} (VCalendar-Parser)
 *
 */
public class VCalendarParseTest {

	/** ein beispielhaftes .ics file */
	private static final String examplePayload = "BEGIN:VCALENDAR\r\n"
			+ "PRODID:-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN\r\n" + "VERSION:2.0\r\n" + "BEGIN:VTIMEZONE\r\n"
			+ "TZID:Europe/Berlin\r\n" + "BEGIN:DAYLIGHT\r\n" + "TZOFFSETFROM:+0100\r\n" + "TZOFFSETTO:+0200\r\n"
			+ "TZNAME:CEST\r\n" + "DTSTART:19700329T020000\r\n" + "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3\r\n"
			+ "END:DAYLIGHT\r\n" + "BEGIN:STANDARD\r\n" + "TZOFFSETFROM:+0200\r\n" + "TZOFFSETTO:+0100\r\n"
			+ "TZNAME:CET\r\n" + "DTSTART:19701025T030000\r\n" + "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10\r\n"
			+ "END:STANDARD\r\n" + "END:VTIMEZONE\r\n" + "BEGIN:VEVENT\r\n" + "CREATED:20221020T213623Z\r\n"
			+ "LAST-MODIFIED:20221020T213634Z\r\n" + "DTSTAMP:20221020T213634Z\r\n"
			+ "UID:ce5935b0-f96c-8346-922c-ac6930faef32\r\n" + "SUMMARY:test\r\n"
			+ "DTSTART;TZID=Europe/Berlin:20221021T230000\r\n" + "DTEND;TZID=Europe/Berlin:20221022T000000\r\n"
			+ "TRANSP:OPAQUE\r\n" + "LOCATION:testo\r\n"
			+ "DESCRIPTION;ALTREP=\"data:text/html,%3 Cbody%3Et esd%3 C%2F body%3E\":tesd\r\n" + "END:VEVENT\r\n"
			+ "END:VCALENDAR\r\n";

	/**
	 * Testet das Parsen eines VCalendars mit {@link VCalendar#parse(String)}
	 */
	@Test
	void testParserWithPayload() {
		VCalendar calendar = VCalendar.parse(examplePayload);
		assertNotNull(calendar.getEvents());
		assertNotNull(calendar.getTimezoneDefinition());
		Instant dtstart = Instant.parse("2022-10-21T21:00:00.000Z");
		Instant dtend = Instant.parse("2022-10-21T22:00:00.000Z");
		assertEquals(dtstart, calendar.getMinDTStart());
		assertEquals(dtend, calendar.getMaxDTEnd());
		assertEquals(VCalendarTyp.VEVENT, calendar.getTyp());
	}

	/**
	 * Testet das Parsen eines Properties mit {@link IProperty#fromString(String)}
	 */
	@Test
	void testParseProperty() {
		String propString = "RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3";
		IProperty fromString = IProperty.fromString(propString);
		assertEquals("RRULE", fromString.getKey());
		assertEquals("FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3", fromString.getValue());
		propString = "DESCRIPTION;ALTREP=\"data:text/html,%3 Cbody%3Et esd%3 C%2F body%3E\":tesd";
		fromString = IProperty.fromString(propString);
		assertEquals("DESCRIPTION;ALTREP=\"data:text/html,%3 Cbody%3Et esd%3 C%2F body%3E\"", fromString.getKey());
		assertEquals("tesd", fromString.getValue());
	}

}
