package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUwUnterrichtsverteilungsdatenWochenstunden extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs   die Wochenstunden des Kurses
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorUwUnterrichtsverteilungsdatenWochenstunden(final @NotNull Supplier<@AllowNull Double> wochenstundenKurs, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUw00UnterrichtsverteilungsdatenWochenstunden(wochenstundenKurs, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
