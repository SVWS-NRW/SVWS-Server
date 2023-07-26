package de.svws_nrw.core.utils;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Datumsberechnungen.
 *
 * @author Benjamin A. Bartsch
 */
public final class DateUtils {

	/** Das kleinste gültige Jahr für das alle Datumsberechnungen geprüft wurden. */
	public static final int MIN_GUELTIGES_JAHR = 1900;

	/** Das größte gültige Jahr für das alle Datumsberechnungen geprüft wurden. */
	public static final int MAX_GUELTIGES_JAHR = 2900;

	/**
	 * Liefert für den jeweiligen Monat im Jahr die Summe der vergangenen Tage.<br>
	 * [0][3] bedeutet, dass im März bereits 59 Tage vergangen sind (kein Schaltjahr).<br>
	 * [1][3] bedeutet, dass im März bereits 60 Tage vergangen sind (Schaltjahr).
	 */
	private static final @NotNull int @NotNull [][] monat_zu_vergangene_tage = new int[][] {
			{0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334}, // kein Schaltjahr
			{0, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335}, // Schaltjahr
	};

	private DateUtils() {
	}

	/**
	 * Liefert ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochenjahr].<br>
	 * <br>
	 * Gültige Werte sind im Bereich:<br>
	 * Index 0: jahr = 1900 bis 2900<br>
	 * Index 1: monat = 1 (Januar) bis 12 (Dezember)<br>
	 * Index 2: tagImMonat = 1 bis 31<br>
	 * Index 3: tagInWoche = 1 (Montag) bis 7 (Sonntag)<br>
	 * Index 4: tagImJahr = 1 bis 365 oder 366 <br>
	 * Index 5: kalenderwoche = 1 bis 52 oder 53<br>
	 * Index 6: kalenderwochenjahr = 1900 bis 2900 (das Jahr der Kalenderwoche)<br>
	 * <br>
	 * Auszug aus Wikipedia: Da ISO 8601 den Montag als ersten Tag der Woche definiert, ist dies somit die erste Woche,
	 * von der mehr Tage (mindestens vier) auf das neue Jahr fallen als auf das alte Jahr.
	 *  Äquivalent hierzu sind die folgenden Definitionen:<br>
     * - jene Woche, die den 4. Januar enthält<br>
     * - jene Woche, die den 1. Januar enthält, falls dieser ein Montag, Dienstag, Mittwoch oder Donnerstag ist, sonst die darauf folgende Woche
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochen, kalenderwochenjahr]..
	 * @see <a href="https://de.wikipedia.org/wiki/Woche#Kalenderwoche">https://de.wikipedia.org/wiki/Woche#Kalenderwoche</a>
	 */
	public static @NotNull int[] extractFromDateISO8601(final @NotNull String datumISO8601) {
		final @NotNull String[] split = datumISO8601.split("-");
		DeveloperNotificationException.ifTrue("Datumsformat von " + datumISO8601 + " ist nicht ISO8601 konform!", split.length != 3);
		final int jahr = DeveloperNotificationException.ifNotInt(split[0]);
		DeveloperNotificationException.ifTrue("Das Jahr von " + datumISO8601 + " ist ungültig!", gibIstJahrUngueltig(jahr));
		final int monat = DeveloperNotificationException.ifNotInt(split[1]);
		DeveloperNotificationException.ifTrue("Der Monat von " + datumISO8601 + " ist ungültig!", (monat < 1) || (monat > 12));
		final int tagImMonat = DeveloperNotificationException.ifNotInt(split[2]);
		DeveloperNotificationException.ifTrue("Der Tag von " + datumISO8601 + " ist ungültig!", (tagImMonat < 1) || (tagImMonat > 31));

		final int schalttage1 = ((jahr - 1) / 4) - ((jahr - 1) / 100) + ((jahr - 1) / 400);
		final int schalttage2 = (jahr / 4) - (jahr / 100) + (jahr / 400);
		final int schaltjahr = schalttage2 - schalttage1;
		final int tagImJahr = monat_zu_vergangene_tage[schaltjahr][monat] + tagImMonat;
		final int tagInWoche = (jahr + schalttage1 + tagImJahr  + 5) % 7 + 1;

		final int tagImJahrAmJanuar4 = 4;
		final int wochentagAmJanuar4 = (jahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		final int tagImJahrAmMontagDerKW1 = tagImJahrAmJanuar4 - wochentagAmJanuar4 + 1;
		final int kalenderwochen = gibKalenderwochenOfJahr(jahr);
		int kalenderwochenjahr = jahr;
		int kalenderwoche = 1 + (tagImJahr - tagImJahrAmMontagDerKW1) / 7;

		// Sonderfall: Datum liegt in der KW des Folgejahres.
		if (kalenderwoche > kalenderwochen) {
			kalenderwoche = 1;
			kalenderwochenjahr++;
		}

		// Sonderfall: Datum liegt in der KW des Vorjahres.
		if (tagImJahr < tagImJahrAmMontagDerKW1) {
			kalenderwoche = gibKalenderwochenOfJahr(jahr - 1);
			kalenderwochenjahr--;
		}

		return new int[] {jahr, monat, tagImMonat, tagInWoche, tagImJahr, kalenderwoche, kalenderwochenjahr};
	}

	/**
	 * Liefert die Anzahl an Kalenderwochen des Jahres (52 oder 53) nach ISO8601.
	 *
	 * @param jahr Das Jahr.
	 *
	 * @return die Anzahl an Kalenderwochen des Jahres (52 oder 53) nach ISO8601.
	 */
	public static int gibKalenderwochenOfJahr(final int jahr) {
		final int schalttage1 = ((jahr - 1) / 4) - ((jahr - 1) / 100) + ((jahr - 1) / 400);
		final int schalttage2 = (jahr / 4) - (jahr / 100) + (jahr / 400);
		final int schaltjahr = schalttage2 - schalttage1;
		final int wochentagAmJanuar1 = (jahr + schalttage1 + 1 + 5) % 7 + 1;
		return (wochentagAmJanuar1 == 4) || ((schaltjahr == 1) && (wochentagAmJanuar1 == 3)) ? 53 : 52;
	}

	/**
	 * Liefert die Anzahl an Tagen des Jahres (365 oder 366).
	 *
	 * @param jahr Das Jahr.
	 *
	 * @return die Anzahl an Tagen des Jahres (365 oder 366).
	 */
	public static int gibTageOfJahr(final int jahr) {
		final int schalttage1 = ((jahr - 1) / 4) - ((jahr - 1) / 100) + ((jahr - 1) / 400);
		final int schalttage2 = (jahr / 4) - (jahr / 100) + (jahr / 400);
		final int schaltjahr = schalttage2 - schalttage1;
		return 365 + schaltjahr;
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd)des Montags der Kalenderwoche des Jahres.
	 * <br>Hinweis: Der Montag kann bei der 1. KW im Vorjahr liegen!
	 * <br>Beispiel 1: Der Montag der 1. KW im 2023 ist der 02.01.2023
	 * <br>Beispiel 2: Der Montag der 1. KW im 2024 ist der 01.01.2024
	 * <br>Beispiel 3: Der Montag der 1. KW im 2025 ist der 30.12.2023!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Montags der Kalenderwoche des Jahres.
	 */
	public static @NotNull String gibDatumDesMontagsOfJahrAndKalenderwoche(final int kalenderwochenjahr, final int kalenderwoche) {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > gibKalenderwochenOfJahr(kalenderwochenjahr));
		// Der 4. Januar ist immer in der 1. Kalenderwoche
		final int schalttage1 = ((kalenderwochenjahr - 1) / 4) - ((kalenderwochenjahr - 1) / 100) + ((kalenderwochenjahr - 1) / 400);
		final int tagImJahrAmJanuar4 = 4;
		final int wochentagAmJanuar4 = (kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		final int tagImJahr = 7 * kalenderwoche - 2 - wochentagAmJanuar4;
		return gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) des Sonntags der Kalenderwoche des Jahres.
	 * <br>Hinweis: Der Sonntag kann bei der 52/53. KW im Folgejahr liegen!
	 * <br>Beispiel 1: Der Sonntag der 52. KW im 2023 ist der 31.12.2023
	 * <br>Beispiel 2: Der Sonntag der 52. KW im 2024 ist der 29.12.2024
	 * <br>Beispiel 3: Der Sonntag der 52. KW im 2025 ist der 28.12.2025
	 * <br>Beispiel 4: Der Sonntag der 53. KW im 2026 ist der 03.01.2027!
	 * <br>Beispiel 5: Der Sonntag der 52. KW im 2027 ist der 02.01.2028!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Sonntags der Kalenderwoche des Jahres.
	 */
	public static @NotNull String gibDatumDesSonntagsOfJahrAndKalenderwoche(final int kalenderwochenjahr, final int kalenderwoche) {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > gibKalenderwochenOfJahr(kalenderwochenjahr));
		// Der 4. Januar ist immer in der 1. Kalenderwoche
		final int schalttage1 = ((kalenderwochenjahr - 1) / 4) - ((kalenderwochenjahr - 1) / 100) + ((kalenderwochenjahr - 1) / 400);
		final int tagImJahrAmJanuar4 = 4;
		final int wochentagAmJanuar4 = (kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		final int tagImJahr = 7 * kalenderwoche + 4 - wochentagAmJanuar4;
		return gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
	}

	/**
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) eines Tages im Jahres.
	 * <br> Hinweis: Die Methode erlaubt Werte die zum Vorjahr oder Folgejahr führen.
	 *
	 * @param jahr       Das Jahr.
	 * @param tagImJahr  Der jeweilige Tag im Jahr. Meistens zwischen 1 und 365, aber nicht zwingend!
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) eines Tages im Jahres.
	 */
	public static @NotNull String gibDatumDesTagesOfJahr(final int jahr, final int tagImJahr) {
		// Korrigiere ggf. das Jahr und den Tag im Jahr.
		int j = jahr;
		int t = tagImJahr;

		if (t <= 0) {
			j--;
			t = t + gibTageOfJahr(j);
		}

		DeveloperNotificationException.ifTrue("Man kann maximal ins Vorjahr springen!", t <= 0);

		if (t > gibTageOfJahr(j)) {
			t = t - gibTageOfJahr(j);
			j++;
		}

		final int tageDesJahres = gibTageOfJahr(j);

		DeveloperNotificationException.ifTrue("Man kann maximal ins Folgejahr springen!", t > tageDesJahres);

		// Der Tag t liegt nun im Bereich 1 bis gibTageOfJahr(j).
		// Berechne nun den zugehörigen Monat "monat".

		final int[] vergangeneTage = (tageDesJahres == 365) ? monat_zu_vergangene_tage[0] : monat_zu_vergangene_tage[1];

		int monat = 12;
		for (int i = 2; i <= 12; i++)
			if (vergangeneTage[i] >= t) {
				monat = i - 1;
				break;
			}

		// Berechne nun den zugehörigen Tag des Monat "tagDesMonats".
		final int tagDesMonats = t - vergangeneTage[monat];

		return StringUtils.padZahl(j, 4) + "-" + StringUtils.padZahl(monat, 2) + "-" + StringUtils.padZahl(tagDesMonats, 2);
	}

	/**
	 * Liefert TRUE, falls das Jahr ungültig ist.
	 *
	 * @param jahr  Das Jahr.
	 *
	 * @return TRUE, falls das Jahr ungültig ist.
	 */
	public static boolean gibIstJahrUngueltig(final int jahr) {
		return (jahr < MIN_GUELTIGES_JAHR) || (jahr > MAX_GUELTIGES_JAHR);
	}

	/**
	 * Liefert anhand der Minuten eine String-Repräsentation der Uhrzeit im Format "hh:mm".
	 * <br>Beispiel: 1000 Minuten --> "16:40"
	 *
	 * @param minuten  Die vergangenen Minuten seit 0 Uhr.
	 *
	 * @return anhand der Minuten eine String-Repräsentation der Uhrzeit im Format "hh:mm".
	 */
	public static @NotNull String getStringOfUhrzeitFromMinuten(final int minuten) {
		DeveloperNotificationException.ifSmaller("minuten", minuten, 0);
		DeveloperNotificationException.ifGreater("minuten", minuten, 24L * 60L);
		final int h = minuten / 60;
		final int m = minuten - h * 60;
		final String sStunden = (h < 10 ? "0" : "") + h;
		final String sMinuten = (m < 10 ? "0" : "") + m;
		return sStunden + ":" + sMinuten;
	}

	/**
	 * Liefert das nach "DIN 5008 optional" konvertierte Datumsformat, z.B. 2023-02-28 zu 28.02.2023.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2023-02-28).
	 *
	 * @return das nach "DIN 5008 optional" konvertierte Datumsformat, z.B. 2023-02-28 zu 28.02.2023.
	 */
	public static @NotNull String gibDatumGermanFormat(final @NotNull String datumISO8601) {
		final @NotNull int[] info = extractFromDateISO8601(datumISO8601);
		final int jahr = info[0];
		final int monat = info[1];
		final int tagImMonat = info[2];
		return StringUtils.padZahl(tagImMonat, 2) + "." + StringUtils.padZahl(monat, 2) + "." + StringUtils.padZahl(jahr, 4);
	}

}
