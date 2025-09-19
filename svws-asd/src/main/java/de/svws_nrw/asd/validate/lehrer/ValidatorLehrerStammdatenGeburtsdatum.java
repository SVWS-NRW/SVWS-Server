package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerStammdatenGeburtsdatum extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenGeburtsdatum(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Geburtsdatum
		@NotNull DateManager geburtsdatum;
		try {
			geburtsdatum = DateManager.from(daten.geburtsdatum);
		} catch (final InvalidDateException e) {
			addFehler(0, "Das Geburtsdatum ist ungültig: " + e.getMessage());
			return false;
		}
		final int schuljahr = kontext().getSchuljahr();
		if (!geburtsdatum.istInJahren(schuljahr - 80, schuljahr - 18)) {
			addFehler(1, "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind die Werte "
					+ (schuljahr - 80) + " bis " + (schuljahr - 18) + ".");
			return false;
		}
		return true;
	}

}
