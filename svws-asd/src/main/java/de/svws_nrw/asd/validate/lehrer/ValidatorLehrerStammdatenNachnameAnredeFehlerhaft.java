package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vor- und Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerStammdatenNachnameAnredeFehlerhaft extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenNachnameAnredeFehlerhaft(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final String nachname = daten.nachname;
		boolean success = true;

		if ((nachname == null) || nachname.isBlank())
			return false;

		final String nLower = nachname.toLowerCase();

		// "frau " im Nachnamen
		final boolean hatFrau = nLower.contains("frau ");

		// "herr " im Nachnamen (Nachname != "herr")
		final boolean hatHerr = nLower.contains("herr ") && !nLower.equals("herr");

		if (hatFrau || hatHerr) {
			addFehler("Bitte entfernen Sie die Anrede (Frau oder Herr) im Feld Nachname. (AA8)");
			success = false;
		}
		return success;
	}

}
