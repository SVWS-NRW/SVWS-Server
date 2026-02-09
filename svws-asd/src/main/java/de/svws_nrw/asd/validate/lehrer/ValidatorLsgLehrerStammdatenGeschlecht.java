package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsgLehrerStammdatenGeschlecht extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsgLehrerStammdatenGeschlecht(final @NotNull Supplier<@AllowNull Integer> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLsg01LehrerStammdatenGeschlecht(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
