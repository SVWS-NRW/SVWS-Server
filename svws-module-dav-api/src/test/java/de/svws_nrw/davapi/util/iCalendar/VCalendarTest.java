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
	private static final String THUNDERBIRD_VCALENDAR_ENTRY = "BEGIN:VCALENDAR\r\n" +
		"PRODID:-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN\r\n" +
		"VERSION:2.0\r\n" +
		"BEGIN:VTIMEZONE\r\n" +
		"TZID:Europe/Berlin\r\n" +
		"BEGIN:DAYLIGHT\r\n" +
		"TZOFFSETFROM:+0100\r\n" +
		"TZOFFSETTO:+0200\r\n" +
		"TZNAME:CEST\r\n" +
		"DTSTART:19700329T020000\r\n" +
		"RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\r\n" +
		"END:DAYLIGHT\r\n" +
		"BEGIN:STANDARD\r\n" +
		"TZOFFSETFROM:+0200\r\n" +
		"TZOFFSETTO:+0100\r\n" +
		"TZNAME:CET\r\n" +
		"DTSTART:19701025T030000\r\n" +
		"RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\r\n" +
		"END:STANDARD\r\n" +
		"END:VTIMEZONE\r\n" +
		"BEGIN:VEVENT\r\n" +
		"CREATED:20221103T065852Z\r\n" +
		"LAST-MODIFIED:20221103T070209Z\r\n" +
		"DTSTAMP:20221103T070209Z\r\n" +
		"UID:3013c8df-740a-4f7e-b5b4-101437edee2d\r\n" +
		"SUMMARY:Test-Termin aus Thunderbird\r\n" +
		"DTSTART;TZID=Europe/Berlin:20221104T124500\r\n" +
		"DTEND;TZID=Europe/Berlin:20221104T134500\r\n" +
		"TRANSP:OPAQUE\r\n" +
		"DESCRIPTION;ALTREP=\"data:text/html,%3Cbody%3EDas%20ist%20ein%20Test-Termin%\r\n" +
		" 20aus%20Thunderbird%3C%2Fbody%3E\":Das ist ein Test-Termin aus Thunderbird\r\n" +
		"SEQUENCE:1\r\n" +
		"END:VEVENT\r\n" +
		"END:VCALENDAR";

	// VCalendar-Eintrag, erstellt mi Outlook
	private static final String OUTLOOK_VCALENDAR_ENTRY = "BEGIN:VCALENDAR\r\n" +
		"VERSION:2.0\r\n" +
		"PRODID:-//ddaysoftware.com//NONSGML DDay.iCal 1.0//EN\r\n" +
		"BEGIN:VTIMEZONE\r\n" +
		"TZID:W. Europe Standard Time\r\n" +
		"BEGIN:STANDARD\r\n" +
		"DTSTART:19701025T030000\r\n" +
		"RRULE:FREQ=YEARLY;BYDAY=-1SU;BYHOUR=3;BYMINUTE=0;BYMONTH=10\r\n" +
		"TZNAME:Mitteleuropäische Zeit\r\n" +
		"TZOFFSETFROM:+0200\r\n" +
		"TZOFFSETTO:+0100\r\n" +
		"END:STANDARD\r\n" +
		"BEGIN:DAYLIGHT\r\n" +
		"DTSTART:19700329T020000\r\n" +
		"RRULE:FREQ=YEARLY;BYDAY=-1SU;BYHOUR=2;BYMINUTE=0;BYMONTH=3\r\n" +
		"TZNAME:Mitteleuropäische Sommerzeit\r\n" +
		"TZOFFSETFROM:+0100\r\n" +
		"TZOFFSETTO:+0200\r\n" +
		"END:DAYLIGHT\r\n" +
		"END:VTIMEZONE\r\n" +
		"BEGIN:VEVENT\r\n" +
		"CLASS:PUBLIC\r\n" +
		"DESCRIPTION:Hallo\\, \\nich bin ein Outlook-Termin.\r\n" +
		"DTEND;TZID=W. Europe Standard Time:20221103T163000\r\n" +
		"DTSTAMP:20221103T084835Z\r\n" +
		"DTSTART;TZID=W. Europe Standard Time:20221103T160000\r\n" +
		"PRIORITY:5\r\n" +
		"SEQUENCE:0\r\n" +
		"SUMMARY:Test-Termin aus Outlook\r\n" +
		"TRANSP:OPAQUE\r\n" +
		"UID:92d53b3d-7b3d-440a-a475-e2b7f63c694c\r\n" +
		"X-MICROSOFT-CDO-BUSYSTATUS:BUSY\r\n" +
		"BEGIN:VALARM\r\n" +
		"ACTION:DISPLAY\r\n" +
		"DESCRIPTION:This is an event reminder\r\n" +
		"TRIGGER:-PT15M\r\n" +
		"END:VALARM\r\n" +
		"END:VEVENT\r\n" +
		"END:VCALENDAR";

	private static String THUNDERBIRD_VCALENDAR_WITH_SPECIAL_CHARACTERS = "BEGIN:VCALENDAR\r\n"
			+ "PRODID:-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN\r\n"
			+ "VERSION:2.0\r\n"
			+ "BEGIN:VTIMEZONE\r\n"
			+ "TZID:Europe/Berlin\r\n"
			+ "BEGIN:DAYLIGHT\r\n"
			+ "TZOFFSETFROM:+0100\r\n"
			+ "TZOFFSETTO:+0200\r\n"
			+ "TZNAME:CEST\r\n"
			+ "DTSTART:19700329T020000\r\n"
			+ "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\r\n"
			+ "END:DAYLIGHT\r\n"
			+ "BEGIN:STANDARD\r\n"
			+ "TZOFFSETFROM:+0200\r\n"
			+ "TZOFFSETTO:+0100\r\n"
			+ "TZNAME:CET\r\n"
			+ "DTSTART:19701025T030000\r\n"
			+ "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\r\n"
			+ "END:STANDARD\r\n"
			+ "END:VTIMEZONE\r\n"
			+ "BEGIN:VEVENT\r\n"
			+ "CREATED:20221103T121605Z\r\n"
			+ "LAST-MODIFIED:20221103T121716Z\r\n"
			+ "DTSTAMP:20221103T121716Z\r\n"
			+ "UID:f0e6de26-f1cf-416c-a46c-31ce415248a6\r\n"
			+ "SUMMARY:testsonderzeichen\"\\,\\;\\\\\r\n"
			+ "DTSTART;TZID=Europe/Berlin:20221103T140000\r\n"
			+ "DTEND;TZID=Europe/Berlin:20221103T150000\r\n"
			+ "TRANSP:OPAQUE\r\n"
			+ "LOCATION:mehr sonderzeichen\"\\,\\;\\\\\r\n"
			+ "DESCRIPTION;ALTREP=\"data:text/html,%3Cbody%3E%0A%3Cdiv%3Eund%20noch%20mehr%\r\n"
			+ " 20sonderzeichen%3Cbr%3E%0Amit%20zeilenumbr%C3%BCchen%3Cbr%3E%0A%22%2C%3B%5C\r\n"
			+ " %3Cbr%3E%0A%C2%A0%5Cr%5Cn%3C%2Fdiv%3E%0A%3C%2Fbody%3E\":und noch mehr sonder\r\n"
			+ " zeichen\\nmit zeilenumbrüchen\\n\"\\,\\;\\\\\\n \\\\r\\\\n\\n\r\n"
			+ "END:VEVENT\r\n"
			+ "END:VCALENDAR";
	
	/**
	 * Testet die Methode {@link VCalendar#parse(String)}}
	 * mit einem VCalendar-Eintrag, der mit Thunderbird
	 * erzeugt wurde.
	 */
	@Test
	void parseThunderbirdVCalendarEntry() {
		VCalendar result = VCalendar.parse(THUNDERBIRD_VCALENDAR_ENTRY);
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
		VCalendar result = VCalendar.parse(OUTLOOK_VCALENDAR_ENTRY);
		assertEquals(1, result.getEvents().size());
		assertEquals(Instant.parse("2022-11-03T16:30:00Z"), result.getEvents().get(0).getDTEnd());
	}
	
	/**
	 * Testet {@link VCalendar#parse(String)} mit verschiedenen Sonderzeichen
	 */
	@Test 
	void parseVCalendarSpecialCharacters() {
		VCalendar result = VCalendar.parse(THUNDERBIRD_VCALENDAR_WITH_SPECIAL_CHARACTERS);
		assertEquals(1, result.getEvents().size());
		assertEquals(Instant.parse("2022-11-03T14:00:00Z"), result.getEvents().get(0).getDTEnd());
	}

}
