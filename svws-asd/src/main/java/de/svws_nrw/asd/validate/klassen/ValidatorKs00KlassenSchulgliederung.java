package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KS00: Prüft, ob für die Klasse eine Schulgliederung angegeben wurde.
 */
public final class ValidatorKs00KlassenSchulgliederung extends Validator {

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKs00KlassenSchulgliederung(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;

		_validatoren.add(new ValidatorKs01KlassenSchulgliederung(klassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		if ((daten == null) || (daten.idSchulgliederung == -1)) {
			addFehler(0, "Schulgliederung der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
