package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KS01: Prüft, ob die angegebene Schulgliederung einer Klasse zulässig ist.
 * Vorbedingung: KS00 (Prüfung auf Vorhandensein) schlägt nicht an.
 */
public final class ValidatorKs01KlassenSchulgliederung extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idSchulgliederung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorKs01KlassenSchulgliederung(
			final @NotNull Supplier<@NotNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;

		_validatoren.add(new ValidatorKs02KlassenSchulgliederung(idSchulgliederung, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idSchulgliederung  = _idSchulgliederung.get();

		if (Schulgliederung.data().getSchluesselByIDOrNull(idSchulgliederung) == null) {
			addFehler(0, "Schulgliederung der Klasse: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
