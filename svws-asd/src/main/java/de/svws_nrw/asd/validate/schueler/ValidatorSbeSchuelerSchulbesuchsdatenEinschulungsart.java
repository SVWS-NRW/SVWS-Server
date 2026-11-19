package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Einschulungsart
 * der Schulbesuchsdaten eines Schülers aus.
 */
public final class ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart(
			final @NotNull Supplier<@AllowNull Long> idEinschulungsartGrundschule,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
