package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IFA: Prüft, ob Förderschwerpunkt des Schülers korrekt ist.
 */
public final class ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog extends Validator {


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIfaIntKatalogFoerderschwerpunkteAsdKatalog(
			final @NotNull Supplier<@AllowNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
