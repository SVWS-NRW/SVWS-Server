package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsdLehrerStammdatenGeburtsdatum extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsdLehrerStammdatenGeburtsdatum(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLsd01LehrerStammdatenGeburtsdatum(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
