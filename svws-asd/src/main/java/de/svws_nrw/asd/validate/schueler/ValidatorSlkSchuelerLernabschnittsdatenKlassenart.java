package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Klassenart eines Schülers aus.
 */
public final class ValidatorSlkSchuelerLernabschnittsdatenKlassenart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart  die Klassenart ID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorSlkSchuelerLernabschnittsdatenKlassenart(
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(
				new ValidatorSlk00SchuelerLernabschnittsdatenKlassenart(getNotNullSupplierLong(idKlassenart), kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}
}
