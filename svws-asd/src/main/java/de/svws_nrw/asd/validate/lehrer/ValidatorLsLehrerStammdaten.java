package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsLehrerStammdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsLehrerStammdaten(final @NotNull LehrerStammdaten daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLsnLehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsvLehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsdLehrerStammdatenGeburtsdatum(daten, kontext));
		_validatoren.add(new ValidatorLsgLehrerStammdatenGeschlecht(daten, kontext));
		_validatoren.add(new ValidatorLskLehrerStammdatenKuerzel(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
