package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOW00: Prüft, ob für die Klasse eine Organisationsform Weiterbildend angegeben wurde.
 */
public final class ValidatorKow00KlassenOrganisationsformWeiterbildung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idWeiterbildendOrganisationsform;

	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public ValidatorKow00KlassenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@AllowNull Long> idWeiterbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;

		_validatoren.add(new ValidatorKow01KlassenOrganisationsformWeiterbildung(getNotNullSupplierLong(idWeiterbildendOrganisationsform), kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == _idWeiterbildendOrganisationsform.get()) {
			addFehler(0, "Organisationsform der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
