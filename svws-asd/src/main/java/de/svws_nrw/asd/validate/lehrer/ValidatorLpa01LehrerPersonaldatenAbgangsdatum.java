package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Personaldaten-Abgangsdatum
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpa01LehrerPersonaldatenAbgangsdatum extends Validator {

	/** Das Abgangsdatum des Lehrers */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Abgangsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLpa01LehrerPersonaldatenAbgangsdatum(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Abgangsdatum
		DateManager abgangsdatum = null;
		@NotNull String errorMsg = "";
		try {
			abgangsdatum = DateManager.from(daten.get());
		} catch (final InvalidDateException e) {
			errorMsg = e.getMessage();
		}
		final DateManager finalAbgangsdatum = abgangsdatum; // wegen Lambda hier nochmal als final.

		if (finalAbgangsdatum == null) {
			this.addFehler(0, "Abgangsdatum des Lehrers: Das Abgangsdatum ist ungültig. " + errorMsg);
			return false;
		}

		return true;
	}

}
