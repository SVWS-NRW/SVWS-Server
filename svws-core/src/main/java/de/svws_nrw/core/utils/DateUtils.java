package de.svws_nrw.core.utils;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für Datumsberechnungen.
 *
 * @author Benjamin A. Bartsch
 */
public final class DateUtils {

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
	 * Auszug aus Wikipedia:: Da ISO 8601 den Montag als ersten Tag der Woche definiert, ist dies somit die erste Woche,
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

		final int tagImJahrAmJanuar4 = monat_zu_vergangene_tage[schaltjahr][1] + 4;
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
	 * Liefert TRUE, falls das Jahr ungültig ist.
	 *
	 * @param jahr  Das Jahr.
	 *
	 * @return TRUE, falls das Jahr ungültig ist.
	 */
	public static boolean gibIstJahrUngueltig(final int jahr) {
		return (jahr < 1900) || (jahr > 2900);
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

}
