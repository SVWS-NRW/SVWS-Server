package de.svws_nrw.asd.validate.schule;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSchuleStammdatenSchulform extends Validator<SchuleStammdaten> {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSchuleStammdatenSchulform(@NotNull final SchuleStammdaten daten, @NotNull final ValidatorKontext kontext) {
		super(daten, kontext);
	}

	@Override
	protected boolean pruefe() {
		final String schulformKrz = daten().schulform;
		if ((schulformKrz == null) || (schulformKrz.isBlank())) {
			addFehler("Die Schulform muss gesetzt sein.");
			return false;
		}
		try {
			Schulform.data().getWertByKuerzel(schulformKrz);
			return true;
		} catch (final CoreTypeException e) {
			addFehler(e.getMessage());
			return false;
		}
	}

}
