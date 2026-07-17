package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die EP-Jahre eines Schülers aus.
 */
public final class ValidatorSleSchuelerLernabschnittsdatenEpJahre extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEpJahre   EP-JahreID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorSleSchuelerLernabschnittsdatenEpJahre(final @NotNull Supplier<@AllowNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSle00SchuelerLernabschnittsdatenEpJahre(idEpJahre, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
