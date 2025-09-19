package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Personalabschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerPersonalabschnittsdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten        die Daten des Validators, hier die Personalabschnittsdaten des Lehrers
	 * @param stammdaten   die Stammdaten des Lehrers
	 * @param kontext      der Kontext des Validators
	 */
	public ValidatorLehrerPersonalabschnittsdaten(final @NotNull LehrerPersonalabschnittsdaten daten, final @NotNull LehrerStammdaten stammdaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		// Die nachfolgenden Prüfungen sind nur durchführbar, wenn bei den Stammdaten ein Geburtsdatum gesetzt ist...
		try {
			final @NotNull DateManager geburtsdatum = DateManager.from(stammdaten.geburtsdatum);
			_validatoren.add(new ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		} catch (@SuppressWarnings("unused") final InvalidDateException e) {
			// Ist kein gültiges Geburtsdatum gesetzt, so werden die Prüfungen übersprungen.
			// Die eigentliche Validierung des Geburtsdatums erfolgt bei den Lehrer-Stammdaten
		}
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
