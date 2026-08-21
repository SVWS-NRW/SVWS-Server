package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IFA: Prüft, ob Förderschwerpunkt des Schülers korrekt ist.
 */
public final class ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idKatalog;

	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIfa00IntKatalogFoerderschwerpunkteAsdKatalog(
			final @NotNull Supplier<@AllowNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog(getNotNullSupplierLong(idKatalog), kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == _idKatalog.get()) {
			addFehler(0, "Förderschwerpunkt des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
