package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung für eine KatalogID des CoreType Religion aus.
 */
public final class ValidatorIkaIntKatalogKonfessionenAsdKatalog extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorIkaIntKatalogKonfessionenAsdKatalog(final @NotNull Supplier<Long> idKatalog, final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIka00IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));

	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
