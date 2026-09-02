package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOO00: Prüft, ob Ort korrekt ist.
 */
public final class ValidatorIoo00IntKatalogOrteOrtsname extends Validator {

	private final @NotNull Supplier<@AllowNull String> ortsname;

	/**
	 * @param plz         die Postleitzahl
	 * @param ortsname    der Name des Ortes
	 * @param idLand      die ID des Landes
	 * @param kontext	  Kontext
	 */
	public ValidatorIoo00IntKatalogOrteOrtsname(
			final @NotNull Supplier<@AllowNull String> plz,
			final @NotNull Supplier<@AllowNull String> ortsname,
			final @NotNull Supplier<@AllowNull Long> idLand,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.ortsname = ortsname;

		_validatoren.add(new ValidatorIoo01IntKatalogOrteOrtsname(getNotNullSupplier(ortsname), idLand, kontext));
		_validatoren.add(new ValidatorIoo10IntKatalogOrteOrtsname(plz, ortsname, idLand, kontext));

	}

	@Override
	protected boolean pruefe() {

		if (null == ortsname.get() || ortsname.get().isEmpty()) {
			addFehler(0, "Das Feld 'Ortsname' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
