package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorSsd01SchuelerStammdatenGeburtsdatum extends Validator {

	/** Das Geburtsdatumm des Schülers */
	private final @NotNull Supplier<String> fieldGeburtsdatum;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsdatumm des Schülers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorSsd01SchuelerStammdatenGeburtsdatum(final @NotNull Supplier<String> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.fieldGeburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Geburtsdatum
		DateManager geburtsdatum = null;
		@NotNull String errorMsg = "";
		try {
			geburtsdatum = DateManager.from(this.fieldGeburtsdatum.get());
		} catch (final InvalidDateException e) {
			errorMsg = e.getMessage();
		}
		final DateManager finalGeburtsdatum = geburtsdatum; //wegen Lambda hier nochmal als final.

		if (finalGeburtsdatum == null) {
			this.addFehler(0, "Das Geburtsdatum ist ungültig: " + errorMsg);
			return false;
		}

		return true;
	}

}
