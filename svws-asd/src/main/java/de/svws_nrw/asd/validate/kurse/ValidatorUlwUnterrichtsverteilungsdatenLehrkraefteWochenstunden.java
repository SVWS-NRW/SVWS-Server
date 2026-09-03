package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden(final @NotNull Supplier<@AllowNull Integer> wochenstundenLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden(wochenstundenLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
