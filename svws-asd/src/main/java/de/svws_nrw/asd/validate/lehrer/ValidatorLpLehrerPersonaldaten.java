package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Personaldaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpLehrerPersonaldaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     	die Daten des Validators
	 * @param stammdaten	die Stammdaten des Lehrers
	 * @param kontext   	der Kontext des Validators
	 */
	public ValidatorLpLehrerPersonaldaten(final @NotNull LehrerPersonaldaten daten, final @NotNull LehrerStammdaten stammdaten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		for (final LehrerPersonalabschnittsdaten abschnittsdaten : daten.abschnittsdaten)
			_validatoren.add(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(abschnittsdaten, stammdaten, kontext));
		try {
			_validatoren.add(new ValidatorLplLehrerPersonaldatenLehramt(daten, DateManager.from(stammdaten.geburtsdatum), kontext));
		} catch (InvalidDateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
