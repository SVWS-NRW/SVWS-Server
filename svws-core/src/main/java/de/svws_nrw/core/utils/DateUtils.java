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

	/** Ein Mapping für den Monat als Zahl zu seiner textuellen Beschreibung. */
	private static final @NotNull String[] MONAT_ZU_TEXT = new String[] {"", "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};

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
	 * Liefert das Datum im ISO8601-Format (uuuu-MM-dd) des Wochentags (aus dem Intervall 1 bis 7) der Kalenderwoche des Kalenderwochenjahres.
	 * <br>Hinweis: Der Montag kann bei der 1. KW im Vorjahr liegen!
	 *
	 * @param kalenderwochenjahr  Das Jahr der Kalenderwoche.
	 * @param kalenderwoche       Die Kalenderwoche.
	 * @param wochentag           Der Wochentag aus dem Intervall 1 bis 7.
	 *
	 * @return das Datum im ISO8601-Format (uuuu-MM-dd) des Wochentags (aus dem Intervall 1 bis 7) der Kalenderwoche des Kalenderwochenjahres.
	 */
	public static @NotNull String gibDatumDesWochentagsOfJahrAndKalenderwoche(final int kalenderwochenjahr, final int kalenderwoche, final int wochentag) {
		DeveloperNotificationException.ifTrue("kalenderwoche < 1", kalenderwoche < 1);
		DeveloperNotificationException.ifTrue("kalenderwoche > gibKalenderwochenOfJahr(jahr)", kalenderwoche > gibKalenderwochenOfJahr(kalenderwochenjahr));
		DeveloperNotificationException.ifTrue("(wochentag < 1) || (wochentag > 7)", (wochentag < 1) || (wochentag > 7));
		// Der 4. Januar ist immer in der 1. Kalenderwoche
		final int schalttage1 = ((kalenderwochenjahr - 1) / 4) - ((kalenderwochenjahr - 1) / 100) + ((kalenderwochenjahr - 1) / 400);
		final int tagImJahrAmJanuar4 = 4;
		final int wochentagAmJanuar4 = (kalenderwochenjahr + schalttage1 + tagImJahrAmJanuar4 + 5) % 7 + 1;
		final int tagImJahr = 7 * kalenderwoche - wochentagAmJanuar4 + wochentag - 3;
		return gibDatumDesTagesOfJahr(kalenderwochenjahr, tagImJahr);
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
		return gibDatumDesWochentagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche, 1);
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
		return gibDatumDesWochentagsOfJahrAndKalenderwoche(kalenderwochenjahr, kalenderwoche, 7);
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

	/**
	 * Liefert das vom Format "2006-08-31" ins Format "31. August 2006" konvertierte Datum.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2023-02-28).
	 *
	 * @return das vom Format "2006-08-31" ins Format "31. August 2006" konvertierte Datum.
	 */
	public static @NotNull String gibDatumGermanFormatAusgeschrieben(final @NotNull String datumISO8601) {
		final @NotNull int[] info = extractFromDateISO8601(datumISO8601);
		final int jahr = info[0];
		final int monat = info[1];
		final int tagImMonat = info[2];
		return tagImMonat + ". " + MONAT_ZU_TEXT[monat] + " " + StringUtils.padZahl(jahr, 4);
	}

	/**
	 * Liefert die Minuten einer Zeitangabe im Format hh:mm.
	 * <br>hh muss ein- oder zweistellig sein, im Bereich 0 bis 23.
	 * <br>mm muss ein- oder zweistellig sein, im Bereich 0 bis 59.
	 *
	 * @param zeit  Die Zeitangabe im Format hh:mm.
	 *
	 * @return die Minuten einer Zeitangabe im Format hh:mm.
	 */
	public static int gibMinutenOfZeitAsString(final @NotNull String zeit) {
		final @NotNull String @NotNull [] sSplit = zeit.split(":");
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm sein!", sSplit.length != 2);

		final @NotNull String sStunden = sSplit[0].trim();
		final @NotNull String sMinuten = sSplit[1].trim();
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm sein!", (sStunden.length() < 1) || (sStunden.length() > 2));
		DeveloperNotificationException.ifTrue("Zeit muss im Format hh:mm sein!", (sMinuten.length() < 1) || (sMinuten.length() > 2));

		final int stunden = Integer.parseInt(sStunden);
		final int minuten = Integer.parseInt(sMinuten);
		DeveloperNotificationException.ifTrue("(stunden < 0) || (stunden > 23)", (stunden < 0) || (stunden > 23));
		DeveloperNotificationException.ifTrue("(minuten < 0) || (minuten > 59)", (minuten < 0) || (minuten > 59));

		return stunden * 60 + minuten;
	}

	/**
	 * Liefert den ZeitString im Format hh:mm zu einer vorgegebenen Minutenanzahl.
	 * <br>Gültige Werte sind im Bereich 0 bis 24*60=1440 (exklusive).
	 *
	 * @param minuten  Die Anzahl der Minuten.
	 *
	 * @return den ZeitString im Format hh:mm zu einer vorgegebenen Minutenanzahl.
	 */
	public static @NotNull String gibZeitStringOfMinuten(final int minuten) {
		DeveloperNotificationException.ifTrue("(minuten < 0) || (minuten >= 1440)", (minuten < 0) || (minuten >= 1440));

		final int std = minuten / 60;
		final int min = minuten - std * 60;

		final @NotNull String sStd = (std < 10 ? "0" : "") + std;
		final @NotNull String sMin = (min < 10 ? "0" : "") + min;

		return sStd + ":" + sMin;
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
		// TODO remove
		return gibZeitStringOfMinuten(minuten);
	}

	/**
	 * Liefert die Kalenderwoche zu einem bestimmten Datum.
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return die Kalenderwoche zu einem bestimmten Datum.
	 */
	public static int gibKwDesDatumsISO8601(final @NotNull String datumISO8601) {
		return extractFromDateISO8601(datumISO8601)[5];
	}

	/**
	 * Liefert das Kalenderwochenjahr zu einem bestimmten Datum.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return die Kalenderwochenjahr zu einem bestimmten Datum.
	 */
	public static int gibKwJahrDesDatumsISO8601(final @NotNull String datumISO8601) {
		return extractFromDateISO8601(datumISO8601)[6];
	}

	/**
	 * Liefert den Wochentag (Mo=1...So=7) zu einem bestimmten Datum.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return den Wochentag (Mo=1...So=7) zu einem bestimmten Datum.
	 */
	public static int gibWochentagDesDatumsISO8601(final @NotNull String datumISO8601) {
		return extractFromDateISO8601(datumISO8601)[3];
	}

	/**
	 * Liefert das Schuljahr zu einem bestimmten Datum. Dabei wird von dem Stichtag des
	 * 1.8. für den Beginn des neuen Schuljahres ausgegangen. Da das Schuljahr immer in
	 * zwei Kalenderjahren liegt, wird immer das erste Kalenderjahr als Schuljahr angegeben.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Schuljahr
	 */
	public static int getSchuljahrFromDateISO8601(final @NotNull String datumISO8601) {
		final int[] iso8601 = extractFromDateISO8601(datumISO8601);
		return (iso8601[1] > 7) ? iso8601[0] : (iso8601[0] - 1);
	}

	/**
	 * Liefert das Halbjahr zu einem bestimmten Datum. Dabei wird zum Einen von dem Stichtag des
	 * 1.8. für den Beginn des neuen Schuljahres ausgegangen und zum Anderen wird vereinfacht (!)
	 * vom 1.2. als Beginn des neuen Halbjahres ausgegangen.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Halbjahr anhand des vereinfachten Kriteriums
	 */
	public static int getHalbjahrFromDateISO8601(final @NotNull String datumISO8601) {
		final int[] iso8601 = extractFromDateISO8601(datumISO8601);
		return (iso8601[1] > 1) && (iso8601[1] < 8) ? 2 : 1;
	}


	/**
	 * Liefert das Schuljahr und das Halbjahr zu einem bestimmten Datum.
	 * Dabei wird zum Einen von dem Stichtag des 1.8. für den Beginn des neuen Schuljahres
	 * ausgegangen und zum Anderen wird vereinfacht (!) vom 1.2. als Beginn des neuen
	 * Halbjahres ausgegangen.
	 *
	 * @param datumISO8601   das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return das Schuljahr (Index 0) und das Halbjahr (Index 1) anhand des vereinfachten Kriteriums
	 */
	public static int[] getSchuljahrUndHalbjahrFromDateISO8601(final @NotNull String datumISO8601) {
		final int[] iso8601 = extractFromDateISO8601(datumISO8601);
		final int[] result = new int[2];
		result[0] = (iso8601[1] > 7) ? iso8601[0] : (iso8601[0] - 1);
		result[1] = (iso8601[1] > 1) && (iso8601[1] < 8) ? 2 : 1;
		return result;
	}

}
