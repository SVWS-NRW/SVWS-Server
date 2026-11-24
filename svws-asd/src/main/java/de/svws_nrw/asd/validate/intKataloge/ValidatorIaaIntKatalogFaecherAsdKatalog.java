package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAA: Prüft das Feld 'Fach ASD-Kürzel'.
 */
public final class ValidatorIaaIntKatalogFaecherAsdKatalog extends Validator {


	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public ValidatorIaaIntKatalogFaecherAsdKatalog(
			final @NotNull Supplier<@AllowNull String> kuerzelStatistik,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIaa00IntKatalogFaecherAsdKatalog(kuerzelStatistik, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
