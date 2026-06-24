package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOA01: Prüft, ob für die Klasse eine Organisationsform Allgemeinbildend zulässig ist.
 * Vorbedingung: KOA00 schlägt nicht an.
 */
public final class ValidatorKoa01KlassenOrganisationsformAllgemeinbildend extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idAllgemeinbildendOrganisationsform;

	/**
	 * @param idAllgemeinbildendOrganisationsform	ID
	 * @param kontext								Kontext
	 */
	public ValidatorKoa01KlassenOrganisationsformAllgemeinbildend(
			final @NotNull Supplier<@NotNull Long> idAllgemeinbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;

		_validatoren.add(new ValidatorKoa02KlassenOrganisationsformAllgemeinbildend(idAllgemeinbildendOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {

		final AllgemeinbildendOrganisationsformen allgemeinbildendOrganisationsform =
				AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(_idAllgemeinbildendOrganisationsform.get());

		if (allgemeinbildendOrganisationsform == null) {
			addFehler(0, "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
