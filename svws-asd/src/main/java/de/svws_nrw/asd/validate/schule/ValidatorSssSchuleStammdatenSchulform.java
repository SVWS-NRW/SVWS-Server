package de.svws_nrw.asd.validate.schule;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSssSchuleStammdatenSchulform extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSssSchuleStammdatenSchulform(final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSss00SchuleStammdatenSchulform(kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
