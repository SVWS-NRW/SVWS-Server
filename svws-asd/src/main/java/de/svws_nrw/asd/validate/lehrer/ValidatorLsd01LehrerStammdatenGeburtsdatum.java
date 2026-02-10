package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsd01LehrerStammdatenGeburtsdatum extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsd01LehrerStammdatenGeburtsdatum(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsd10LehrerStammdatenGeburtsdatum(getNotNullSupplier(daten), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Geburtsdatum
		DateManager geburtsdatum = null;
		@NotNull String errorMsg = "";
		try {
			geburtsdatum = DateManager.from(daten.get());
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
