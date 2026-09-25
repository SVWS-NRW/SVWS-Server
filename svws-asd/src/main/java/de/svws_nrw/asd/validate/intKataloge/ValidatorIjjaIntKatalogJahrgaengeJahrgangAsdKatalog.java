package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IJJA: Prüft, ob Jahrgang des internen Kataloges korrekt ist.
 */
public final class ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIjjaIntKatalogJahrgaengeJahrgangAsdKatalog(
			final @NotNull Supplier<@AllowNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIjja00IntKatalogJahrgaengeJahrgangAsdKatalog(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
