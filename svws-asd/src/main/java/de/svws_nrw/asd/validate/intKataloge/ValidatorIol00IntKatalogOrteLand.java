package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOL00: Prüft, ob Land korrekt ist.
 */
public final class ValidatorIol00IntKatalogOrteLand extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idKatalog;

	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public ValidatorIol00IntKatalogOrteLand(
			final @NotNull Supplier<@AllowNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIol01IntKatalogOrteLand(getNotNullSupplierLong(idKatalog), kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == _idKatalog.get()) {
			addFehler(0, "Das Feld 'Land' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
