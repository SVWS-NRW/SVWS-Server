package de.svws_nrw.asd.validate.lehrer;

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
public final class ValidatorLsd10LehrerStammdatenGeburtsdatum extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geburtsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsd10LehrerStammdatenGeburtsdatum(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Geburtsdatum
		DateManager geburtsdatum = null;
			try {
				geburtsdatum = DateManager.from(daten.get());
			} catch (InvalidDateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		final DateManager finalGeburtsdatum = geburtsdatum; //wegen Lambda hier nochmal als final.

		final int schuljahr = kontext().getSchuljahr();

		if (finalGeburtsdatum == null || !finalGeburtsdatum.istInJahren(schuljahr - 80, schuljahr - 18)) {
			this.addFehler(1, "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte " + (schuljahr - 80) + " bis " + (schuljahr - 18) + ".");
			return false;
		}
		return true;
	}

}
