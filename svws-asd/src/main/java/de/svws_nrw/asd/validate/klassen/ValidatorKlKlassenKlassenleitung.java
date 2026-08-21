package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator Kl: Prüft die Klassenleitungen.
 */
public final class ValidatorKlKlassenKlassenleitung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenLeitungen   Klassenleitungen
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorKlKlassenKlassenleitung(
			final @NotNull Supplier<@NotNull List<Long>> klassenLeitungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKl10KlassenKlassenleitung(klassenLeitungen, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
