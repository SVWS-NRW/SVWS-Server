package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsd00LehrerStammdatenGeburtsdatum extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsd00LehrerStammdatenGeburtsdatum(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsd01LehrerStammdatenGeburtsdatum(daten, kontext));
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;

		// Bestimme das Geburtsdatum
		DateManager geburtsdatum = null;
		@NotNull String errorMsg = "";
		try {
			geburtsdatum = DateManager.from(daten.geburtsdatum);
		} catch (final InvalidDateException e) {
			errorMsg = e.getMessage();
		}
		final DateManager finalGeburtsdatum = geburtsdatum; //wegen Lambda hier nochmal als final.

		success = exec(0, () -> finalGeburtsdatum == null, "Das Geburtsdatum ist ungültig: " + errorMsg);
		if (!success)
			return false;

		return success;
	}

}
