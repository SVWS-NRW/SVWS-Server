package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}


	@Override
	protected boolean pruefe() {
		final Double pflichtstundensoll = daten.pflichtstundensoll;
		if (pflichtstundensoll == null) {
			addFehler(0, "Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}
		boolean success = true;
		if ((pflichtstundensoll < 0.0) || (pflichtstundensoll > 41.0)) {
			addFehler(1, "Unzulässiger Wert im Feld 'pflichtstundensoll'. Zulässig sind im Stundenmodell Werte im Bereich von 0,00 bis 41,00 Wochenstunden. "
					+ "Im Minutenmodell zwischen 0,00 und 1845,00 Minuten.");
			success = false;
		}
		final LehrerEinsatzstatus einsatzstatus = LehrerEinsatzstatus.getBySchluessel(daten.einsatzstatus);
		if ((einsatzstatus == LehrerEinsatzstatus.B) && (pflichtstundensoll == 0.0)) {
			addFehler(2, "Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'B'), darf das Pflichtstundensoll"
					+ " nicht 0,00 betragen.");
			success = false;
		}
		return success;
	}

}
