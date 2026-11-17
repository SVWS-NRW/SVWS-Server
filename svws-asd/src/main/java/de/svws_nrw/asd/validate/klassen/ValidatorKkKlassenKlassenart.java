package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Klassenart
 * einer Klasse aus.
 */
public final class ValidatorKkKlassenKlassenart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKlassenart                KlassenartID
	 * @param listIdKlassenartSchueler    Liste der IDs der Klassenart der Schüler dieser Klasse
	 * @param kontext                     der Kontext des Validators
	 */
	public ValidatorKkKlassenKlassenart(
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull Supplier<@AllowNull List<Long>> listIdKlassenartSchueler,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKk00KlassenKlassenart(idKlassenart, kontext));
		_validatoren.add(new ValidatorKk20KlassenKlassenart(idKlassenart, listIdKlassenartSchueler, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
