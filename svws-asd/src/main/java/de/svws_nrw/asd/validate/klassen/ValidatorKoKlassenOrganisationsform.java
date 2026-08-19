package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Obervalidator führt eine Statistikprüfung auf die Organisationsform
 * einer Klasse aus.
 */
public final class ValidatorKoKlassenOrganisationsform extends Validator {

	/**
	 * @param idAllgemeinbildendOrganisationsform	ID Orgaform A-Schulen
	 * @param idWeiterbildendOrganisationsform	    ID Orgaform WBK-Schulen
	 * @param idBerufsbildendOrganisationsform	    ID Orgaform BK-Schulen
	 * @param kontext							    Kontext
	 */
	public ValidatorKoKlassenOrganisationsform(
			final @NotNull Supplier<@AllowNull Long> idAllgemeinbildendOrganisationsform,
			final @NotNull Supplier<@AllowNull Long> idWeiterbildendOrganisationsform,
			final @NotNull Supplier<@AllowNull Long> idBerufsbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorKoaKlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
		_validatoren.add(new ValidatorKowKlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
		_validatoren.add(new ValidatorKobKlassenOrganisationsformBerufsbildend(idBerufsbildendOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
