package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
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

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKs02KlassenSchulgliederung(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		if (daten == null) {
			return true;
		}

		final Schulgliederung gliederung = Schulgliederung.data().getWertByIDOrNull(daten.idSchulgliederung);

		if (gliederung == null) {
			return true;
		}

		// Prüfen, ob für das aktuelle Schuljahr ein gültiger Historieneintrag existiert.
		if (gliederung.daten(kontext().getSchuljahr()) == null) {
			addFehler(0,
					"Schulgliederung der Klasse: Der eingetragene Wert für das Feld 'Schulgliederung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
