package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsgSchuelerStammdatenGeschlecht extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht     das Geschlecht des Schuelers
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorSsgSchuelerStammdatenGeschlecht(final @NotNull Supplier<@AllowNull Integer> idGeschlecht,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsg00SchuelerStammdatenGeschlecht(idGeschlecht, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
