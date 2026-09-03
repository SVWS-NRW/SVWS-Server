package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOP00: Prüft, ob PLZ korrekt ist.
 */
public final class ValidatorIop00IntKatalogOrtePlz extends Validator {

	private final @NotNull Supplier<@AllowNull String> plz;

	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public ValidatorIop00IntKatalogOrtePlz(
			final @NotNull Supplier<@AllowNull String> plz,
			final @NotNull Supplier<@AllowNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.plz = plz;

		_validatoren.add(new ValidatorIop01IntKatalogOrtePlz(getNotNullSupplier(plz), idLand, kontext));
		_validatoren.add(new ValidatorIop10IntKatalogOrtePlz(getNotNullSupplier(plz), idLand, kontext));
		_validatoren.add(new ValidatorIoo10IntKatalogOrteOrtsname(plz, ortsname, idLand, kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == plz.get() || plz.get().isEmpty()) {
			addFehler(0, "Das Feld 'PLZ' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
