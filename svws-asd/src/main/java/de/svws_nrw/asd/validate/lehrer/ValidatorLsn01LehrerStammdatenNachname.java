package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsn01LehrerStammdatenNachname extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn01LehrerStammdatenNachname(final @NotNull LehrerStammdaten daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsn02LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn03LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn04LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn05LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn06LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn07LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn08LehrerStammdatenNachname(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;
		final String nachname = daten.nachname;

		success = exec(1, () -> nachname.trim().isBlank(), "Nachname der Lehrkraft: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
		if (!success)
			return false;

		return success;
	}

}
