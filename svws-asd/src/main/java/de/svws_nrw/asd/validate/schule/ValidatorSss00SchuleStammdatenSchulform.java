package de.svws_nrw.asd.validate.schule;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSss00SchuleStammdatenSchulform extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSss00SchuleStammdatenSchulform(final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSss01SchuleStammdatenSchulform(kontext));
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;

		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final String schulformKrz = super.kontext().getSchuleStammdaten().schulform;
		System.out.println("schulform:" + schulformKrz + "#");
		success = exec(0, () -> (schulformKrz == null) || (schulformKrz.isBlank()), "Die Schulform muss gesetzt sein.");
		if (!success)
			return false;

		return true;
	}

}
