package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Personalabschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerPersonalabschnittdaten extends Validator<LehrerPersonalabschnittsdaten> {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerPersonalabschnittdaten(@NotNull final LehrerPersonalabschnittsdaten daten, @NotNull final ValidatorKontext kontext) {
		super(daten, kontext);
		_validatoren.add(new ValidatorLehrerPersonalabschnittdatenPflichtstundensoll(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
