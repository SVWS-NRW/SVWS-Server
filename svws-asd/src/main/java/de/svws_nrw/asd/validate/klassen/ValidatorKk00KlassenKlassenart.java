package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK00: Prüft, ob für die Klasse eine Klassenart angegeben wurde.
 */
public final class ValidatorKk00KlassenKlassenart extends Validator {

	private final @NotNull Supplier<KlassenDaten> _klassenDaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param klassenDaten   ein Supplier für die Klassendaten
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorKk00KlassenKlassenart(
			final @NotNull Supplier<KlassenDaten> klassenDaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._klassenDaten = klassenDaten;

		_validatoren.add(new ValidatorKk01KlassenKlassenart(klassenDaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final KlassenDaten daten = _klassenDaten.get();

		// idKlassenart ist vom Typ Long, der Default ist null.
		if ((daten == null) || (daten.idKlassenart == null)) {
			addFehler(0, "Art der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
