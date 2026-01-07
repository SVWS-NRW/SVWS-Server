package de.svws_nrw.asd.validate;

import java.util.Set;

import jakarta.validation.constraints.NotNull;

/**
 * Dieser Manager stellt Methoden zum Umgang mit Datumswerten zur Verfügung.
 * Die Daten werden im Input und Output jeweils im ISO 8601-Format (yyyy-mm-dd) erwartet.
 * Für die Lesbarkeit in Textausgaben steht auch die Möglichkeit des deutschen
 * Datumsformates zur Verfügung.
 */
public final class NamensManager {

	/**
	 *  Namenszusätze in Europa:
	 *  Ist noch mit IT.NRW abzustimmen, was davon umgesetzt werden soll.
	 *  Deutschsprachiger Raum : von, zu, vom, vonder, zum, zur
	 *  Niederlande : van, van de, van der, van den, de, ten
	 *  Belgien : de, de la, de l’, van, van der,
	 *  Frankreich : de, du, des, de la, le, la
	 *  Spanien : de, del, de la, los, las, y
	 *  Italien : di, della, del, dei, da
	 *  Portugal : de, da, do, dos, das
	 *  Großbritannien : of, ap (walisisch), fitz (anglo-normannisch),
	 *  Skandinavien : af, von, son, dotter
	 *  Polen : z, de
	 *  Ungarn : de, von, fi
	 *  Russland und Osteuropa : von, de
	 *
	 *  Gesamt:
	 *  1-teilig: af, ap, da, das, de, dei, del, della, des, di, do, dos, dotter, du, fi, fitz, la, las, le, los, of, son, ten, van, vom, von, vonder, y, z, zu, zum, zur
	 *  2-teilig: de la, de l’, van de, van den, van der
	 */
	// Die Menge der einstelligen Zusätze, welche bei der Prüfung des Anfangsbuchstabens gefiltert werden
	private static final @NotNull Set<String> zusaetze = Set.of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	// Die Menge der zwei stelligen Zusätze, welche bei der Prüfung des Anfangsbuchstabens gefiltert werden
	private static final @NotNull Set<String> zusaetzeZweiteilig = Set.of("de la");

	/**
	 * Entfernt ggf. die in "zusaetze" oder "zusaetzeZweiteilig" aufgeführten Zusätze, welche dem Nachnamen
	 * vorangestellt sein können. Diese Methode wird zur Prüfung des Anfangsbuchstabens des Nachnamens
	 * verwendet.
	 *
	 * @param nachname   der Nachname
	 *
	 * @return der Nachname mit ggf. entferntem Vornamen
	 */
	public static @NotNull String getOhneZusatz(final String nachname) {
		if (nachname == null)
			return "";
		final @NotNull String @NotNull [] teile = nachname.split(" ", 3);
		if (teile.length == 3 && zusaetzeZweiteilig.contains(teile[0] + " " + teile[1]))
			return teile[2];
		if (teile.length == 3 && zusaetze.contains(teile[0]))
			return teile[1] + " " + teile[2];
		if (teile.length == 2 && zusaetze.contains(teile[0]))
			return teile[1];
		return nachname;
	}
}
