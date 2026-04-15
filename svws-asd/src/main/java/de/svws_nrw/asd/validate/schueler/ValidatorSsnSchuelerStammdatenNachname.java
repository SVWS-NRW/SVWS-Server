package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachname bei den Stammdaten
 * eines Schülers aus.
 */
public final class ValidatorSsnSchuelerStammdatenNachname extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param nachname         der Nachname des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorSsnSchuelerStammdatenNachname(final @NotNull Supplier<@AllowNull String> nachname,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsn00SchuelerStammdatenNachname(nachname, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
