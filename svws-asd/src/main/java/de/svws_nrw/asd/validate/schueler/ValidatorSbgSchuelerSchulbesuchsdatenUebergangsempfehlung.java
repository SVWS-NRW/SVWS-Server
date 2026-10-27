package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Übergangsempfehlung
 * der Schulbesuchsdaten eines Schülers aus.
 */
public final class ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idUebergangsempfehlungGrundschule    die ID der Übergangsempfehlung Grundschule des Schülers
	 * @param kontext                              der Kontext des Validators
	 */
	public ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung(
			final @NotNull Supplier<@AllowNull Long> idUebergangsempfehlungGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSbg00SchuelerSchulbesuchsdatenUebergangsempfehlung(idUebergangsempfehlungGrundschule, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
