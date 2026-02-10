package de.svws_nrw.asd.validate.schule;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten einer Schule aus.
 */
public final class ValidatorSss00SchuleStammdatenSchulform extends Validator {

	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Schulform
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSss00SchuleStammdatenSchulform(final @NotNull Supplier<@AllowNull String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorSss01SchuleStammdatenSchulform(getNotNullSupplier(daten), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final String schulformKrz = daten.get();

		if ((schulformKrz == null) || schulformKrz.isBlank()) {
			addFehler(0, "Die Schulform muss gesetzt sein.");
			return false;
		}

		return true;
	}

}
