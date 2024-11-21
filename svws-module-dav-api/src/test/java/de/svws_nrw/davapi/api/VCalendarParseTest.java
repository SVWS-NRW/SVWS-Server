package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.svws_nrw.davapi.util.icalendar.IProperty;
import de.svws_nrw.davapi.util.icalendar.VCalendar;
import de.svws_nrw.davapi.util.icalendar.VCalendarTyp;

/**
 * Testklasse für {@link VCalendar} (VCalendar-Parser)
 *
 */
class VCalendarParseTest {

	/** ein beispielhaftes .ics file */
	private static final String examplePayload = """
		BEGIN:VCALENDAR\r
		PRODID:-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN\r
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
		CREATED:20221020T213623Z\r
		LAST-MODIFIED:20221020T213634Z\r
		DTSTAMP:20221020T213634Z\r
		UID:ce5935b0-f96c-8346-922c-ac6930faef32\r
		SUMMARY:test\r
		DTSTART;TZID=Europe/Berlin:20221021T230000\r
		DTEND;TZID=Europe/Berlin:20221022T000000\r
		TRANSP:OPAQUE\r
		LOCATION:testo\r
		DESCRIPTION;ALTREP=\"data:text/html,%3 Cbody%3Et esd%3 C%2F body%3E\":tesd\r
		END:VEVENT\r
		END:VCALENDAR\r
		""";

	/**
	 * Testet das Parsen eines VCalendars mit {@link VCalendar#parse(String)}
	 */
	@Test
	void testParserWithPayload() {
		final VCalendar calendar = VCalendar.parse(examplePayload);
		assertNotNull(calendar.getEvents());
		assertNotNull(calendar.getTimezoneDefinition());
		final Instant dtstart = Instant.parse("2022-10-21T21:00:00.000Z");
		final Instant dtend = Instant.parse("2022-10-21T22:00:00.000Z");
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
