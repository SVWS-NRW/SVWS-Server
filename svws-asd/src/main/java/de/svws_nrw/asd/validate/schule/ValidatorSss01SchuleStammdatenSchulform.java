package de.svws_nrw.asd.validate.schule;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSss01SchuleStammdatenSchulform extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSss01SchuleStammdatenSchulform(final @NotNull ValidatorKontext kontext) {
		super(kontext);
	}

	@Override
	protected boolean pruefe() {

		final String schulformKrz = super.kontext().getSchuleStammdaten().schulform;
		// Prüfe, ob die Schulform gültig gesetzt ist oder nicht

		try {
			return Schulform.data().getWertByKuerzel(schulformKrz) == null;
		} catch (@SuppressWarnings("unused") final CoreTypeException e) {
			this.addFehler(1, "Das Kürzel für die Schulform ist ungültig.");
			return false;
		}
	}

}
