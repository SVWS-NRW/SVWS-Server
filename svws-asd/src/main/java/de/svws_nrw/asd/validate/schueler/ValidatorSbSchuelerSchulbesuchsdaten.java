package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Schulbesuchsdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSbSchuelerSchulbesuchsdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idKuerzelGrundschuleUebergangsempfehlung    GrundschuleUebergangsempfehlung ID
	 * @param kontext        				              der Kontext des Validators
	 */
	public ValidatorSbSchuelerSchulbesuchsdaten(
			final @NotNull Supplier<@AllowNull Long> idKuerzelGrundschuleUebergangsempfehlung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSbgSchuelerSchulbesuchsdatenUebergangsempfehlung(idKuerzelGrundschuleUebergangsempfehlung, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
