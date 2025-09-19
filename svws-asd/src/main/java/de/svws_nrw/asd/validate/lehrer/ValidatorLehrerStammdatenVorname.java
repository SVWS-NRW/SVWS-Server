package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerStammdatenVorname extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenVorname(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}


	@Override
	protected boolean pruefe() {
		final String vorname = daten.vorname;
		if ((vorname == null) || (vorname.length() == 0)) {
			addFehler(0, "Vorname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}
		if (vorname.trim().isBlank()) {
			addFehler(1, "Vorname der Lehrkraft: Der Vorname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		boolean success = true;
		if ((daten.vorname.length() == 1)) {
			addFehler(2, "Vorname der Lehrkraft: Der Vorname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			success = false;
		}
		if (vorname.startsWith(" ") || vorname.startsWith("\t")) {
			addFehler(3, "Vorname der Lehrkraft: Die Eintragung des Nachnamens muss linksbündig erfolgen (ohne vorangestellte Leerzeichen oder Tabs).");
			success = false;
		}
		if (!Character.isUpperCase(vorname.charAt(0))) {
			addFehler(4, "Vorname der Lehrkraft: Die erste Stelle des Vornamens muss mit einem Großbuchstaben besetzt sein.");
			success = false;
		}
		if ((vorname.length() > 1) && Character.isUpperCase(vorname.charAt(1))) {
			addFehler(5,
					"Vorname der Lehrkraft: Die zweite Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if ((vorname.length() > 2) && Character.isUpperCase(vorname.charAt(2))) {
			addFehler(6,
					"Vorname der Lehrkraft: Die dritte Stelle des Vornamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Vornamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.");
			success = false;
		}
		if (vorname.contains(" -") || vorname.contains("- ")) {
			addFehler(7, "Vorname der Lehrkraft: Der Vorname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			success = false;
		}

		// Der Vorname darf nicht mit Frau oder Herr anfangen.
		final String nLower = vorname.toLowerCase();
		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			addFehler(8, "Vorname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Vornamen.");
			success = false;
		}
		return success;
	}

}
