package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAA: Prüft das Feld 'Fach ASD-Kürzel'.
 */
public final class ValidatorIaa01IntKatalogFaecherAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull String> _kuerzelStatistik;

	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public ValidatorIaa01IntKatalogFaecherAsdKatalog(
			final @NotNull Supplier<@NotNull String> kuerzelStatistik,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;

		_validatoren.add(new ValidatorIaa02IntKatalogFaecherAsdKatalog(kuerzelStatistik, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Fach fach = Fach.getBySchluesselOrDefault(_kuerzelStatistik.get());

		if (Fach.VF == fach) {
			addFehler(0, "Fach: Das Feld 'Fach ASD-Kürzel' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
