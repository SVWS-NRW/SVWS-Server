package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Organisationsform Weiterbildend
 * einer Klasse aus.
 */
public final class ValidatorKowKlassenOrganisationsformWeiterbildung extends Validator {

	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public ValidatorKowKlassenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@AllowNull Long> idWeiterbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKow00KlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
