package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsv10LehrerStammdatenVorname extends Validator {

	/** Der Lehrer-Vorname */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsv10LehrerStammdatenVorname(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsv11LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv12LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv13LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv14LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv15LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv16LehrerStammdatenVorname(daten, kontext));
		_validatoren.add(new ValidatorLsv17LehrerStammdatenVorname(daten, kontext));

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
