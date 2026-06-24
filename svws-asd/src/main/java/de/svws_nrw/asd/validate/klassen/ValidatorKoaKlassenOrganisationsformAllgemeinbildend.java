package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Organisationsform Allgemeinbildend
 * einer Klasse aus.
 */
public final class ValidatorKoaKlassenOrganisationsformAllgemeinbildend extends Validator {

	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public ValidatorKoaKlassenOrganisationsformAllgemeinbildend(
			final @NotNull Supplier<@AllowNull Long> idAllgemeinbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKoa00KlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
