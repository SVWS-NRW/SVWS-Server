package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOA00: Prüft, ob für die Klasse eine Organisationsform Allgemeinbildend angegeben wurde.
 */
public final class ValidatorKoa00KlassenOrganisationsformAllgemeinbildend extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idAllgemeinbildendOrganisationsform;

	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public ValidatorKoa00KlassenOrganisationsformAllgemeinbildend(
			final @NotNull Supplier<@AllowNull Long> idAllgemeinbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;

		_validatoren.add(new ValidatorKoa01KlassenOrganisationsformAllgemeinbildend(getNotNullSupplierLong(idAllgemeinbildendOrganisationsform), kontext));
	}

	@Override
	protected boolean pruefe() {

		if (null == _idAllgemeinbildendOrganisationsform.get()) {
			addFehler(0, "Organisationsform der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
