package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Schülers aus.
 */
public final class ValidatorSsdSchuelerStammdatenGeburtsdatum extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsadtum des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorSsdSchuelerStammdatenGeburtsdatum(final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsd00SchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
