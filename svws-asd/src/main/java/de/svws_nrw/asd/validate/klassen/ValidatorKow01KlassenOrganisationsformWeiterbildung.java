package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOW01: Prüft, ob für die Klasse eine Organisationsform Weiterbildend zulässig ist.
 * Vorbedingung: KOW00 schlägt nicht an.
 */
public final class ValidatorKow01KlassenOrganisationsformWeiterbildung extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idWeiterbildendOrganisationsform;

	/**
	 * @param idWeiterbildendOrganisationsform	ID
	 * @param kontext							Kontext
	 */
	public ValidatorKow01KlassenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@NotNull Long> idWeiterbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;

		_validatoren.add(new ValidatorKow02KlassenOrganisationsformWeiterbildung(idWeiterbildendOrganisationsform, kontext));
	}

	@Override
	protected boolean pruefe() {

		final WeiterbildungskollegOrganisationsformen idWeiterbildendOrganisationsform =
				WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(_idWeiterbildendOrganisationsform.get());

		if (idWeiterbildendOrganisationsform == null) {
			addFehler(0, "Organisationsform der Klasse: Das Feld 'Organisationsform' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
