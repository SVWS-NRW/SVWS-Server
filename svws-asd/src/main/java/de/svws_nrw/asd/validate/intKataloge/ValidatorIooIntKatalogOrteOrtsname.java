package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOO: Prüft, ob Ort korrekt ist.
 */
public final class ValidatorIooIntKatalogOrteOrtsname extends Validator {


	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext     Kontext
	 */
	public ValidatorIooIntKatalogOrteOrtsname(
			final @NotNull Supplier<@AllowNull String> plz,
			final @NotNull Supplier<@AllowNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorIoo00IntKatalogOrteOrtsname(plz, ortsname, idLand, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
