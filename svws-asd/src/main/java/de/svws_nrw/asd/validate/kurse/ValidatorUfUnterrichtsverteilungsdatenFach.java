package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Fach des Kurses aus.
 */
public final class ValidatorUfUnterrichtsverteilungsdatenFach extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public ValidatorUfUnterrichtsverteilungsdatenFach(final @NotNull Supplier<@AllowNull Long> idFach,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorUf00UnterrichtsverteilungsdatenFach(idFach, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
