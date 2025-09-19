package de.svws_nrw.asd.validate.schule;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSchuleStammdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSchuleStammdaten(final @NotNull ValidatorKontext kontext) {
		super(kontext);
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform gültig gesetzt ist oder nicht
		final String schulformKrz = super.kontext().getSchuleStammdaten().schulform;
		if ((schulformKrz == null) || (schulformKrz.isBlank())) {
			addFehler(0, "Die Schulform muss gesetzt sein.");
			return false;
		}
		try {
			Schulform.data().getWertByKuerzel(schulformKrz);
		} catch (@SuppressWarnings("unused") final CoreTypeException e) {
			addFehler(1, "Das Kürzel für die Schulform ist ungültig.");
			return false;
		}
		return true;
	}

}
