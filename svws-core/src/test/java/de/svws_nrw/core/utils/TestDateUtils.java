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
@DisplayName("Diese Klasse enthält die Testroutinen für die Klasse {@link DateUtils}.")
class TestDateUtils {


    /**
     * Initialisiert den Test
     */
    @BeforeAll
    static void setup() {
    	// leer
    }

    /**
     * Testet die Methode {@link DateUtils#extractFromDateISO8601(String)}
     * @throws ParseException falls das Datum nicht geparsed werden kann.
     *
     */
	@Test
	@DisplayName("Testet die Methode extractFromDateISO8601")
    void testExtractFromDateISO8601() throws ParseException {
		final Calendar cal = new GregorianCalendar(Locale.GERMANY);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String datumCurrent = "1900-01-01";
		cal.setTime(sdf.parse(datumCurrent));
		while (true) {
			final int jahr = cal.get(Calendar.YEAR);

			if (DateUtils.gibIstJahrUngueltig(jahr))
				break;

			final int monat = cal.get(Calendar.MONTH) + 1;
			final int tagImMonat = cal.get(Calendar.DAY_OF_MONTH);
			final int tagInWoche = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;
			final int tagImJahr = cal.get(Calendar.DAY_OF_YEAR);
			final int kalenderwoche = cal.get(Calendar.WEEK_OF_YEAR);
			int kalenderwochenjahr = jahr;
			if ((monat == 1) && (kalenderwoche >= 52)) {
				kalenderwochenjahr = jahr - 1;
			}
			if ((monat == 12) && (kalenderwoche == 1)) {
				kalenderwochenjahr = jahr + 1;
			}

			final int[] info = DateUtils.extractFromDateISO8601(datumCurrent);
			assertEquals(jahr, info[0]);
			assertEquals(monat, info[1]);
			assertEquals(tagImMonat, info[2]);
			assertEquals(tagInWoche, info[3]);
			assertEquals(tagImJahr, info[4]);
			assertEquals(kalenderwoche, info[5]);
			assertEquals(kalenderwochenjahr, info[6]);
			assertEquals(datumCurrent, DateUtils.gibDatumDesTagesOfJahr(jahr, tagImJahr));

			// Springe zum nächsten Tag.
			cal.add(Calendar.DATE, +1);
			datumCurrent = sdf.format(cal.getTime());
		}
    }

	 /**
     * Testet die Methode {@link DateUtils#extractFromDateISO8601(String)}
     * @throws ParseException falls das Datum nicht geparsed werden kann.
     *
     */
	@Test
	@DisplayName("Testet die Methode gibDatumDesMontags/SonntagsOfJahrAndKalenderwoche")
    void testMontagUndFreitagOfKalenderwoche() throws ParseException {
		final int jahrMin = DateUtils.MIN_GUELTIGES_JAHR + 1; // Da es Zugriff auf das Vorjahr geben kann.
		final int jahrMax = DateUtils.MAX_GUELTIGES_JAHR - 1; // Da es Zugriff auf das Folgejahr geben kann.

		for (int kalenderwochenjahr = jahrMin; kalenderwochenjahr <= jahrMax; kalenderwochenjahr++) {
			for (int kalenderwoche = 1; kalenderwoche <= DateUtils.gibKalenderwochenOfJahr(kalenderwochenjahr); kalenderwoche++) {
				final String sMo = DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche);
				final String sSo = DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche);
				final int[] infoMo = DateUtils.extractFromDateISO8601(sMo);
				final int[] infoSo = DateUtils.extractFromDateISO8601(sSo);
				assertEquals(kalenderwochenjahr, infoMo[6]);
				assertEquals(kalenderwochenjahr, infoSo[6]);
				assertEquals(kalenderwoche, infoMo[5]);
				assertEquals(kalenderwoche, infoSo[5]);
			}
		}
	}

}
