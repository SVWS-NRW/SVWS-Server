package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAB: Prüft, ob Bilinguale Sachfachsprache des internen Kataloges korrekt ist.
 */
public final class ValidatorIabIntKatalogFaecherBilingualeSprache extends Validator {


	/**
	 * @param idBilingualeSprache   ID
	 * @param kontext	            Kontext
	 */
	public ValidatorIabIntKatalogFaecherBilingualeSprache(
			final @NotNull Supplier<@AllowNull Long> idBilingualeSprache,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIab01IntKatalogFaecherBilingualeSprache(idBilingualeSprache, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
