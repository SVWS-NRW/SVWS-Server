package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerStammdatenNachname extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenNachname(final @NotNull LehrerStammdaten daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

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
	private final @NotNull Set<String> zusaetze = Set.of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	// Die Menge der zwei stelligen Zusätze, welche bei der Prüfung des Anfangsbuchstabens gefiltert werden
	private final @NotNull Set<String> zusaetzeZweiteilig = Set.of("de la");

	/**
	 * Entfernt ggf. die in "zusaetze" oder "zusaetzeZweiteilig" aufgeführten Zusätze, welche dem Nachnamen
	 * vorangestellt sein können. Diese Methode wird zur Prüfung des Anfangsbuchstabens des Nachnamens
	 * verwendet.
	 *
	 * @param nachname   der Nachname
	 *
	 * @return der Nachname mit ggf. entferntem Vornamen
	 */
	private @NotNull String getOhneZusatz(final @NotNull String nachname) {
		final @NotNull String @NotNull [] teile = nachname.split(" ", 3);
		if ((teile.length == 3) && (zusaetzeZweiteilig.contains(teile[0] + " " + teile[1])))
			return teile[2];
		if ((teile.length == 3) && (zusaetze.contains(teile[0])))
			return teile[1] + " " + teile[2];
		if ((teile.length == 2) && (zusaetze.contains(teile[0])))
			return teile[1];
		return nachname;
	}


	@Override
	protected boolean pruefe() {
		final String nachname = daten.nachname;
		if ((nachname == null) || (nachname.length() == 0)) {
			addFehler(0, "Nachname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		if (nachname.trim().isBlank()) {
			addFehler(1, "Nachname der Lehrkraft: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		boolean success = true;
		if (daten.nachname.length() == 1) {
			addFehler(2, "Nachname der Lehrkraft: Der Nachname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			success = false;
		}
		if (nachname.startsWith(" ") || nachname.startsWith("\t")) {
			addFehler(3, "Nachname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			success = false;
		}
		final @NotNull String nachnameOhneZusatz = getOhneZusatz(nachname);
		if (!Character.isUpperCase(nachnameOhneZusatz.charAt(0))) {
			addFehler(4,
					"Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein.");
			success = false;
		}
		if ((nachnameOhneZusatz.length() > 1) && Character.isUpperCase(nachnameOhneZusatz.charAt(1))) {
			addFehler(5,
					"Nachname der Lehrkraft: Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if ((nachnameOhneZusatz.length() > 2) && Character.isUpperCase(nachnameOhneZusatz.charAt(2))
				&& !Set.of("A'", "D'", "M'", "O'", "Mc").contains(nachnameOhneZusatz.substring(0, 2))) {
			addFehler(6,
					"Nachname der Lehrkraft: Die dritte Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if (nachname.contains(" -") || nachname.contains("- ")) {
			addFehler(7, "Nachname der Lehrkraft: Der Nachname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			success = false;
		}

		// Der Nachname darf nicht mit Frau oder Herr anfangen.
		final String nLower = nachname.toLowerCase();
		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			addFehler(8, "Nachname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Nachnamen.");
			success = false;
		}

		return success;
	}

}
