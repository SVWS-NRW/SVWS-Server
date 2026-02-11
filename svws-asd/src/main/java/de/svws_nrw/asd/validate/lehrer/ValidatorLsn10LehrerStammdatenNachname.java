package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsn10LehrerStammdatenNachname extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn10LehrerStammdatenNachname(final @NotNull Supplier<String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsn11LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn12LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn13LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn14LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn15LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn16LehrerStammdatenNachname(daten, kontext));
		_validatoren.add(new ValidatorLsn17LehrerStammdatenNachname(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @NotNull String nachname = daten.get();

		if (nachname.isBlank()) {
			addFehler(1, "Nachname der Lehrkraft: Der Nachname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		return true;
	}

}
