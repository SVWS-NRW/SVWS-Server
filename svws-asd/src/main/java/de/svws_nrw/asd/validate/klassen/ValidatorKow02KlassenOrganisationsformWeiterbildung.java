package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOW02: Prüft, ob für die Klasse eine Organisationsform Weiterbildend gültig ist.
 * Vorbedingung: KOW01 schlägt nicht an.
 */
public final class ValidatorKow02KlassenOrganisationsformWeiterbildung extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idWeiterbildendOrganisationsform;

	/**
	 * @param idWeiterbildendOrganisationsform 	ID
	 * @param kontext							Kontext
	 */
	public ValidatorKow02KlassenOrganisationsformWeiterbildung(
			final @NotNull Supplier<@NotNull Long> idWeiterbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idWeiterbildendOrganisationsform = idWeiterbildendOrganisationsform;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!WeiterbildungskollegOrganisationsformen.data().isGueltig(_idWeiterbildendOrganisationsform.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
