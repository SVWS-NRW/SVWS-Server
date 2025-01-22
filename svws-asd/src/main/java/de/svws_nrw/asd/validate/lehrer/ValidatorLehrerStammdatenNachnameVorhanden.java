package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus, ob ein Nachname bei den Stammdaten
 * eines Lehrers einer Schule angegeben wurde oder nicht.
 */
public final class ValidatorLehrerStammdatenNachnameVorhanden extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull LehrerStammdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerStammdatenNachnameVorhanden(final @NotNull LehrerStammdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final String nachname = daten.nachname;
		if ((nachname == null) || (nachname.length() == 0)) {
			addFehler("Kein Wert im Feld 'nachname'.");
			return false;
		}
		if (nachname.trim().isBlank()) {
			addFehler("Das Feld 'nachname' darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

}
