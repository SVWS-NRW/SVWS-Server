package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KOA02: Prüft, ob für die Klasse eine Organisationsform Allgemeinbildend gültig ist.
 * Vorbedingung: KOA01 schlägt nicht an.
 */
public final class ValidatorKoa02KlassenOrganisationsformAllgemeinbildend extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idAllgemeinbildendOrganisationsform;

	/**
	 * @param idAllgemeinbildendOrganisationsform 	ID
	 * @param kontext								Kontext
	 */
	public ValidatorKoa02KlassenOrganisationsformAllgemeinbildend(
			final @NotNull Supplier<@NotNull Long> idAllgemeinbildendOrganisationsform,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idAllgemeinbildendOrganisationsform = idAllgemeinbildendOrganisationsform;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (!AllgemeinbildendOrganisationsformen.data().isGueltig(_idAllgemeinbildendOrganisationsform.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Organisationsform der Klasse: Der eingetragene Wert für das Feld 'Organisationsform' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
