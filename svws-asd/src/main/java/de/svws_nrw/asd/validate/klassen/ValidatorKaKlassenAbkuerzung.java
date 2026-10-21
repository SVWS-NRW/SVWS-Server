package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf das Kürzel der Klasse
 * einer Klasse aus.
 */
public final class ValidatorKaKlassenAbkuerzung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel             Kürzel der Klasse
	 * @param listKuerzel         Liste der Klassenkürzel
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorKaKlassenAbkuerzung(
			final @NotNull Supplier<@AllowNull String> kuerzel,
			final @NotNull Supplier<@AllowNull List<String>> listKuerzel,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKa00KlassenAbkuerzung(kuerzel, listKuerzel, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
