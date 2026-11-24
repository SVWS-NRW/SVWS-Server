package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IAA: Prüft das Feld 'Fach ASD-Kürzel'.
 */
public final class ValidatorIaa00IntKatalogFaecherAsdKatalog extends Validator {

	private final @NotNull Supplier<@AllowNull String> _kuerzelStatistik;

	/**
	 * @param kuerzelStatistik    Fach ASD-Kürzel
	 * @param kontext	          Kontext
	 */
	public ValidatorIaa00IntKatalogFaecherAsdKatalog(
			final @NotNull Supplier<@AllowNull String> kuerzelStatistik,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._kuerzelStatistik = kuerzelStatistik;

		_validatoren.add(new ValidatorIaa01IntKatalogFaecherAsdKatalog(getNotNullSupplier(kuerzelStatistik), kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == _kuerzelStatistik.get() || _kuerzelStatistik.get().isEmpty()) {
			addFehler(0, "Fach: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
