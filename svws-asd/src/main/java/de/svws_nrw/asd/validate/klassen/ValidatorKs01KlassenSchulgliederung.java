package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KS01: Prüft, ob die angegebene Schulgliederung einer Klasse zulässig ist.
 * Vorbedingung: KS00 (Prüfung auf Vorhandensein) schlägt nicht an.
 */
public final class ValidatorKs01KlassenSchulgliederung extends Validator {

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKs01KlassenSchulgliederung(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;

		_validatoren.add(new ValidatorKs02KlassenSchulgliederung(klassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		if (daten == null) {
			return true;
		}

		final Schulgliederung gliederung = Schulgliederung.data().getWertByIDOrNull(daten.idSchulgliederung);

		if (gliederung == null) {
			addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}

		// hatSchulform prüft, ob die Schulgliederung für das aktuelle Schuljahr und die Schulform der Schule zulässig ist
		if (!gliederung.hatSchulform(kontext().getSchuljahr(), kontext().getSchulform())) {
			addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
