package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus, ob dieser nur einstellig ist.
 */
public final class ValidatorLskLehrerStammdatenKuerzel extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<@AllowNull String> daten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLskLehrerStammdatenKuerzel(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsk10LehrerStammdatenKuerzel(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
