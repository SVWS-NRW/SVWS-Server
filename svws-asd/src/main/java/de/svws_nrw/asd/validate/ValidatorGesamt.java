package de.svws_nrw.asd.validate;

import de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt;
import de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdaten;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf alle Daten einer Schule aus.
 */
public final class ValidatorGesamt extends Validator<SchuleStatistikdatenGesamt> {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorGesamt(final @NotNull SchuleStatistikdatenGesamt daten, final @NotNull ValidatorKontext kontext) {
		super(daten, kontext);
		_validatoren.add(new ValidatorSchuleStammdaten(daten.stammdaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}