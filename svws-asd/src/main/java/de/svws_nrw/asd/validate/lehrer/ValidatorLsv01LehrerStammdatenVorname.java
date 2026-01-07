package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsv01LehrerStammdatenVorname extends Validator {

	/** Der Lehrer-Vorname */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsv01LehrerStammdatenVorname(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsv02LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv03LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv04LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv05LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv06LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv07LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv08LehrerStammdatenVorname(daten, kontext));

	}


	@Override
	protected boolean pruefe() {
		if (daten.get().trim().isBlank()) {
			addFehler(1, "Vorname der Lehrkraft: Der Vorname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		return true;
	}

}
