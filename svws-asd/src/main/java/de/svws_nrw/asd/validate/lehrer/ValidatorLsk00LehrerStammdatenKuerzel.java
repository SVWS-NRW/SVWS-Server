package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus, ob dieser nur einstellig ist.
 */
public final class ValidatorLsk00LehrerStammdatenKuerzel extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsk00LehrerStammdatenKuerzel(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		// Prüfe zunächst, ob überhaupt ein Kürzel vorhanden ist
		if ((daten.kuerzel == null) || daten.kuerzel.isBlank())
			return true; // Dieser Fall wird von anderen Validatoren gehandhabt, weshalb die Prüfung hier nicht fehlschlägt

		String fehlertext0 = "Der Eintrag " + daten.kuerzel + " ist als Lehrerkürzel unzulässig."
				+ " Zulässig sind: 1. Stelle: A-Z, Ä, Ö, Ü; 2.-4. Stelle: A-Z, Ä, Ö, Ü, -, 'kein Eintrag'."
				+ " Buchstaben müssen großgeschrieben werden.";

		if (!daten.kuerzel.matches("^[A-ZÄÖÜ][A-ZÄÖÜ0-9\\-\\ ]{0,3}$")) {
			this.addFehler(0, fehlertext0);
			return false;
		}
		return true;
	}

}
