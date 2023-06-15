package de.svws_nrw.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diese Klasse enthält die Testroutinen für die Klasse {@link DateUtils}.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Diese Klasse enthält die Testroutinen für die Klasse {@link DateUtils}")
class TestDateUtils {


    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    	// leer
    }

    /**
     * Testet die Methode {@link DateUtils#convert(String)}
     * @throws ParseException falls das Datum nicht geparsed werden kann.
     *
     */
	@Test
	@DisplayName("Testet die Methode convert")
    void testConvert() throws ParseException {
		final Calendar cal = new GregorianCalendar(Locale.GERMAN);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String datumCurrent = "1900-01-01";
		cal.setTime(sdf.parse(datumCurrent));
		while (true) {
			final int jahr = cal.get(Calendar.YEAR);
			if (jahr == 2901) break;
			final int monat = cal.get(Calendar.MONTH) + 1;
			final int tag = cal.get(Calendar.DAY_OF_MONTH);
			final int wochentag = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;

			final int[] info = DateUtils.convert(datumCurrent);
			assertEquals(jahr, info[0]);
			assertEquals(monat, info[1]);
			assertEquals(tag, info[2]);
			assertEquals(wochentag, info[3]);

			// Springe zum nächsten Tag.
			cal.add(Calendar.DATE, +1);
			datumCurrent = sdf.format(cal.getTime());
		}

    }

}
