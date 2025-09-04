package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus, ob dieser nur einstellig ist.
 */
public final class ValidatorLehrerStammdatenVornameNurEinstellig extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenVornameNurEinstellig(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		// Prüfe zunächst, ob überhaupt ein Name vorhanden ist
		if ((daten.vorname == null) || (daten.vorname.length() == 0) || daten.vorname.isBlank()) {
			return true; // Dieser Fall wird von anderen Validatoren gehandhabt, weshalb die Prüfung hier nicht fehlschlägt
		}
		boolean success = true;
		if ((daten.vorname.length() == 1)) {
			addFehler("Der Vorname besteht aus nur einem Zeichen. Bitte überprüfen sie ihre Angaben.");
			success = false;
		}
		return success;
	}

}
