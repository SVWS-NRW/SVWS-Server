package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vor- und Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerStammdatenVornameAnredeFehlerhaft extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenVornameAnredeFehlerhaft(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final String vorname = daten.vorname;
		boolean success = true;

		if ((vorname == null) || vorname.isBlank())
			return false;

		final String vLower = vorname.toLowerCase();

		// "frau " im Vornamen (Vorname != "frauke")
		final boolean hatFrau = vLower.contains("frau ") && !vLower.equals("frauke");

		// "herr " im Vornamen (Vorname != "herr")
		final boolean hatHerr = vLower.contains("herr ") && !vLower.equals("herr");

		if (hatFrau || hatHerr) {
			addFehler("Bitte entfernen Sie die Anrede (Frau oder Herr) im Feld Vorname. (AA8)");
			success = false;
		}
		return success;
	}

}
