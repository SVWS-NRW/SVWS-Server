package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KS02: Prüft, ob die angegebene Schulgliederung einer Klasse
 * im ausgewählten Schuljahr zeitlich gültig ist.
 * Vorbedingung: KS01 (Prüfung auf grundsätzliche Zulässigkeit) schlägt nicht an.
 */
public final class ValidatorKs02KlassenSchulgliederung extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idSchulgliederung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorKs02KlassenSchulgliederung(
			final @NotNull Supplier<@NotNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;
	}

	@Override
	protected boolean pruefe() {

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
				if (!Schulgliederung.data().isGueltig(_idSchulgliederung.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Schulgliederung der Klasse: Der eingetragene Wert für das Feld 'Schulgliederung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
