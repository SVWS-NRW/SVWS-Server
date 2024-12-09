package de.svws_nrw.davapi.util.iCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.svws_nrw.davapi.util.icalendar.VCalendar;

/**
 * Diese Klasse enthält die Testroutinen für den VCalendar-Parser
 */
class VCalendarTest {

	//V Calendar-Eintrag, erstellt mi Thunderbird
	private static final String THUNDERBIRD_VCALENDAR_ENTRY = """
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
		RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\r
		END:DAYLIGHT\r
		BEGIN:STANDARD\r
		TZOFFSETFROM:+0200\r
		TZOFFSETTO:+0100\r
		TZNAME:CET\r
		DTSTART:19701025T030000\r
		RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\r
		END:STANDARD\r
		END:VTIMEZONE\r
		BEGIN:VEVENT\r
		CREATED:20221103T065852Z\r
		LAST-MODIFIED:20221103T070209Z\r
		DTSTAMP:20221103T070209Z\r
		UID:3013c8df-740a-4f7e-b5b4-101437edee2d\r
		SUMMARY:Test-Termin aus Thunderbird\r
		DTSTART;TZID=Europe/Berlin:20221104T124500\r
		DTEND;TZID=Europe/Berlin:20221104T134500\r
		TRANSP:OPAQUE\r
		DESCRIPTION;ALTREP=\"data:text/html,%3Cbody%3EDas%20ist%20ein%20Test-Termin%\r
		 20aus%20Thunderbird%3C%2Fbody%3E\":Das ist ein Test-Termin aus Thunderbird\r
		SEQUENCE:1\r
		END:VEVENT\r
		END:VCALENDAR\r
		""";

	// VCalendar-Eintrag, erstellt mi Outlook
	private static final String OUTLOOK_VCALENDAR_ENTRY = """
		BEGIN:VCALENDAR\r
		VERSION:2.0\r
		PRODID:-//ddaysoftware.com//NONSGML DDay.iCal 1.0//EN\r
		BEGIN:VTIMEZONE\r
		TZID:W. Europe Standard Time\r
		BEGIN:STANDARD\r
		DTSTART:19701025T030000\r
		RRULE:FREQ=YEARLY;BYDAY=-1SU;BYHOUR=3;BYMINUTE=0;BYMONTH=10\r
		TZNAME:Mitteleuropäische Zeit\r
		TZOFFSETFROM:+0200\r
		TZOFFSETTO:+0100\r
		END:STANDARD\r
		BEGIN:DAYLIGHT\r
		DTSTART:19700329T020000\r
		RRULE:FREQ=YEARLY;BYDAY=-1SU;BYHOUR=2;BYMINUTE=0;BYMONTH=3\r
		TZNAME:Mitteleuropäische Sommerzeit\r
		TZOFFSETFROM:+0100\r
		TZOFFSETTO:+0200\r
		END:DAYLIGHT\r
		END:VTIMEZONE\r
		BEGIN:VEVENT\r
		CLASS:PUBLIC\r
		DESCRIPTION:Hallo\\, \\nich bin ein Outlook-Termin.\r
		DTEND;TZID=W. Europe Standard Time:20221103T163000\r
		DTSTAMP:20221103T084835Z\r
		DTSTART;TZID=W. Europe Standard Time:20221103T160000\r
		PRIORITY:5\r
		SEQUENCE:0\r
		SUMMARY:Test-Termin aus Outlook\r
		TRANSP:OPAQUE\r
		UID:92d53b3d-7b3d-440a-a475-e2b7f63c694c\r
		X-MICROSOFT-CDO-BUSYSTATUS:BUSY\r
		BEGIN:VALARM\r
		ACTION:DISPLAY\r
		DESCRIPTION:This is an event reminder\r
		TRIGGER:-PT15M\r
		END:VALARM\r
		END:VEVENT\r
		END:VCALENDAR\r
		""";

	private static final String THUNDERBIRD_VCALENDAR_WITH_SPECIAL_CHARACTERS = """
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
		RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\r
		END:DAYLIGHT\r
		BEGIN:STANDARD\r
		TZOFFSETFROM:+0200\r
		TZOFFSETTO:+0100\r
		TZNAME:CET\r
		DTSTART:19701025T030000\r
		RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\r
		END:STANDARD\r
		END:VTIMEZONE\r
		BEGIN:VEVENT\r
		CREATED:20221103T121605Z\r
		LAST-MODIFIED:20221103T121716Z\r
		DTSTAMP:20221103T121716Z\r
		UID:f0e6de26-f1cf-416c-a46c-31ce415248a6\r
		SUMMARY:testsonderzeichen\"\\,\\;\\\\\r
		DTSTART;TZID=Europe/Berlin:20221103T140000\r
		DTEND;TZID=Europe/Berlin:20221103T150000\r
		TRANSP:OPAQUE\r
		LOCATION:mehr sonderzeichen\"\\,\\;\\\\\r
		DESCRIPTION;ALTREP=\"data:text/html,%3Cbody%3E%0A%3Cdiv%3Eund%20noch%20mehr%\r
		 20sonderzeichen%3Cbr%3E%0Amit%20zeilenumbr%C3%BCchen%3Cbr%3E%0A%22%2C%3B%5C\r
		 %3Cbr%3E%0A%C2%A0%5Cr%5Cn%3C%2Fdiv%3E%0A%3C%2Fbody%3E\":und noch mehr sonder\r
		 zeichen\\nmit zeilenumbrüchen\\n\"\\,\\;\\\\\\n \\\\r\\\\n\\n\r
		END:VEVENT\r
		END:VCALENDAR\r
		""";

	/**
	 * Testet die Methode {@link VCalendar#parse(String)}}
	 * mit einem VCalendar-Eintrag, der mit Thunderbird
	 * erzeugt wurde.
	 */
	@Test
	void parseThunderbirdVCalendarEntry() {
		final VCalendar result = VCalendar.parse(THUNDERBIRD_VCALENDAR_ENTRY);
		assertEquals(1, result.getEvents().size());
		assertEquals(Instant.parse("2022-11-04T12:45:00Z"), result.getEvents().get(0).getDTEnd());
	}

	/**
	 * Testet die Methode {@link VCalendar#parse(String)}}
	 * mit einem VCalendar-Eintrag, der mit Outlook
	 * erzeugt wurde.
	 */
	@Test
	void parseOutlookVCalendarEntry() {
		final VCalendar result = VCalendar.parse(OUTLOOK_VCALENDAR_ENTRY);
		assertEquals(1, result.getEvents().size());
		assertEquals(Instant.parse("2022-11-03T16:30:00Z"), result.getEvents().get(0).getDTEnd());
	}

	/**
	 * Testet {@link VCalendar#parse(String)} mit verschiedenen Sonderzeichen
	 */
	@Test
	void parseVCalendarSpecialCharacters() {
		final VCalendar result = VCalendar.parse(THUNDERBIRD_VCALENDAR_WITH_SPECIAL_CHARACTERS);
		assertEquals(1, result.getEvents().size());
		assertEquals(Instant.parse("2022-11-03T14:00:00Z"), result.getEvents().get(0).getDTEnd());
	}

}
