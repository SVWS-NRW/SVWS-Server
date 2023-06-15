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
	 * Liefert ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tag, wochentag].<br>
	 * Gültige Werte sind im Bereich:<br>
	 * Index 0: jahr = 1900 bis 2900<br>
	 * Index 1: monat = 1 (Januar) bis 12 (Dezember)<br>
	 * Index 2: tag = 1 bis 31<br>
	 * Index 3: wochentag = 1 (Montag) bis 7 (Sonntag)<br>
	 *
	 * TODO BAR KW
	 *
	 * @param datumISO8601 Das Datum im ISO8601-Format uuuu-MM-dd (z.B. 2014-03-14).
	 *
	 * @return ein aus dem Datum extrahiertes int[] mit dem Inhalt [jahr, monat, tag, wochentag].
	 */
	public static @NotNull int[] convert(final @NotNull String datumISO8601) {
		final @NotNull String[] split = datumISO8601.split("-");
		DeveloperNotificationException.ifTrue("Datumsformat von " + datumISO8601 + " ist nicht ISO8601 konform!", split.length != 3);
		final int jahr = DeveloperNotificationException.ifNotInt(split[0]);
		DeveloperNotificationException.ifTrue("Das Jahr von " + datumISO8601 + " ist ungültig!", (jahr < 1900) || (jahr > 2900));
		final int monat = DeveloperNotificationException.ifNotInt(split[1]);
		DeveloperNotificationException.ifTrue("Der Monat von " + datumISO8601 + " ist ungültig!", (monat < 1) || (monat > 12));
		final int tag = DeveloperNotificationException.ifNotInt(split[2]);
		DeveloperNotificationException.ifTrue("Der Tag von " + datumISO8601 + " ist ungültig!", (tag < 1) || (tag > 31));

		final int schalttage1 = ((jahr - 1) / 4) - ((jahr - 1) / 100) + ((jahr - 1) / 400); // Summe Schalttage vom Jahr 0 bis jahr-1
		final int schalttage2 = (jahr / 4) - (jahr / 100) + (jahr / 400);                   // Summe Schalttage vom Jahr 0 bis jahr
		final int schaltjahr = schalttage2 - schalttage1;
		final int wochentag = (jahr + schalttage1 + monat_zu_vergangene_tage[schaltjahr][monat] + tag  + 5) % 7 + 1;
		return new int[] {jahr, monat, tag, wochentag};
	}
}
