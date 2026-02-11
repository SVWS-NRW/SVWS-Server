package de.svws_nrw.asd.validate.schule;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSssSchuleStammdatenSchulform extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Schulform
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSssSchuleStammdatenSchulform(final @NotNull Supplier<@AllowNull String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSss00SchuleStammdatenSchulform(daten, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
