package de.svws_nrw.davapi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import de.svws_nrw.davapi.util.icalendar.DateTimeUtil;
import de.svws_nrw.davapi.util.icalendar.IProperty;

/**
 * Testklasse für die {@link DateTimeUtil}
 *
 */
public class DateTimeUtilTest {

	/**
	 * Testet das Parsen eines CalDav ISO InputStrings mit Zeitzone anhand von
	 * {@link DateTimeUtil#parseCalDav(String)}
	 */
	@Test
	void testParseCalDavWithTimeZoneAppended() {
		String input = "20220702T000000Z";
		Instant parsedCalDav = DateTimeUtil.parseCalDav(input);
		assertEquals(Instant.parse("2022-07-02T00:00:00.000Z"), parsedCalDav);

		input = "20220702T000000+02";
		parsedCalDav = DateTimeUtil.parseCalDav(input);
		assertEquals(Instant.parse("2022-07-02T00:00:00.000+02:00"), parsedCalDav);
	}

	/**
	 * Testet das Parsen eines {@link IProperty} mit
	 * {@link DateTimeUtil#parseCalDav(IProperty)}
	 */
	@Test
	void testParseCalDavWithProperty() {
		IProperty fromString = IProperty.fromString("DTSTART;TZID=Europe/Berlin:20220702T120000");
		Instant parseCalDav = DateTimeUtil.parseCalDav(fromString);
		assertEquals(Instant.parse("2022-07-02T10:00:00.000Z"), parseCalDav);
		assertEquals(LocalDateTime.parse("2022-07-02T12:00:00.000").atZone(ZoneId.of(DateTimeUtil.TIMEZONE_DEFAULT))
				.toInstant(), parseCalDav);

		// Tag ändert sich durch Zeitzonenkonvertierung
		fromString = IProperty.fromString("DTSTART;TZID=Europe/Berlin:20220702T000000");
		parseCalDav = DateTimeUtil.parseCalDav(fromString);
		assertEquals(Instant.parse("2022-07-01T22:00:00.000Z"), parseCalDav);

		// jahr ändert sich durch zeitzonenänderung
		fromString = IProperty.fromString("DTSTART;TZID=Europe/Berlin:20220101T000000");
		parseCalDav = DateTimeUtil.parseCalDav(fromString);
		assertEquals(Instant.parse("2021-12-31T23:00:00.000Z"), parseCalDav);
	}

	/**
	 * testet das Parsen eines {@link IProperty} in der Form die Outlook nutzt mit
	 * {@link DateTimeUtil#parseCalDav(IProperty)}
	 */
	@Test
	void testParseCalDavWithPropertyOutlook() {
		// MS Outlook nutzt andere Zeitzonen-IDs:
		// Potenzieller Fehler: “The datetime zone id ‘W. Europe Standard Time’ is not
		// recognised
		IProperty fromString = IProperty.fromString("DTSTART;TZID=W. Europe Standard Time:20220702T100000");
		Instant parseCalDav = DateTimeUtil.parseCalDav(fromString);
		assertEquals(Instant.parse("2022-07-02T10:00:00.000Z"), parseCalDav);
		assertEquals(LocalDateTime.parse("2022-07-02T12:00:00.000").atZone(ZoneId.of(DateTimeUtil.TIMEZONE_DEFAULT))
				.toInstant(), parseCalDav);

	}

	/**
	 * Testet die Methode {@link DateTimeUtil#between(Instant, Instant, Instant)}
	 */
	@Test
	void testBetween() {
		Instant t1 = DateTimeUtil.parseCalDav("20220701T000000Z");
		Instant t2 = DateTimeUtil.parseCalDav("20220702T000000Z");
		Instant t3 = DateTimeUtil.parseCalDav("20220703T000000Z");
		assertTrue(DateTimeUtil.between(t1, t3, t2));
		assertFalse(DateTimeUtil.between(t1, t2, t3));
		assertFalse(DateTimeUtil.between(t2, t3, t1));
		assertTrue(DateTimeUtil.between(t1, t2, t2));
		assertTrue(DateTimeUtil.between(t1, t2, t1));
		assertTrue(DateTimeUtil.between(t1, t1, t1));
	}

	/**
	 * Testet die Methode
	 * {@link DateTimeUtil#intersect(Instant, Instant, Instant, Instant)}
	 */
	@Test
	void testIntersect() {
		Instant t1 = DateTimeUtil.parseCalDav("20220701T000000Z");
		Instant t2 = DateTimeUtil.parseCalDav("20220702T000000Z");
		Instant t3 = DateTimeUtil.parseCalDav("20220703T000000Z");
		Instant t4 = DateTimeUtil.parseCalDav("20220704T000000Z");

		// überschneidend:
		// ---
		// .---
		assertTrue(DateTimeUtil.intersect(t1, t3, t2, t4));
		// überschneidend:
		// -
		// .---
		assertTrue(DateTimeUtil.intersect(t1, t2, t2, t4));
		// überschneidend:
		// ----
		// .--
		assertTrue(DateTimeUtil.intersect(t1, t4, t2, t3));
		// überschneidend:
		// .--
		// ----
		assertTrue(DateTimeUtil.intersect(t2, t3, t1, t4));
		// nicht überschneidend
		// ---
		// ....---
		assertFalse(DateTimeUtil.intersect(t1, t2, t3, t4));
		// nicht überschneidend
		// ..--
		// --..
		assertFalse(DateTimeUtil.intersect(t3, t4, t1, t2));
	}
}
