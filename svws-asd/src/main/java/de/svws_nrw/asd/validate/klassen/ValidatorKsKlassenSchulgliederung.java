package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Schulgliederung
 * einer Klasse aus.
 */
public final class ValidatorKsKlassenSchulgliederung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorKsKlassenSchulgliederung(
			final @NotNull Supplier<@AllowNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKs00KlassenSchulgliederung(idSchulgliederung, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
