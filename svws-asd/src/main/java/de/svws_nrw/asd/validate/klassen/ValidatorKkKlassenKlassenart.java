package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Klassenart
 * einer Klasse aus.
 */
public final class ValidatorKkKlassenKlassenart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten, die geprüft werden sollen
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKkKlassenKlassenart(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKk00KlassenKlassenart(klassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
