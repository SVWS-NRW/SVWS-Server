package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK01: Prüft, ob die angegebene Klassenart einer Klasse zulässig ist.
 */
public final class ValidatorKk01KlassenKlassenart extends Validator {

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKk01KlassenKlassenart(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;

		_validatoren.add(new ValidatorKk02KlassenKlassenart(klassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		if ((daten == null) || (daten.idKlassenart == null)) {
			return true;
		}

		final Klassenart art = Klassenart.data().getWertByIDOrNull(daten.idKlassenart);

		if (art == null) {
			addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}

		if (!art.hatSchulform(kontext().getSchuljahr(), kontext().getSchulform())) {
			addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}

		return true;
	}
}
