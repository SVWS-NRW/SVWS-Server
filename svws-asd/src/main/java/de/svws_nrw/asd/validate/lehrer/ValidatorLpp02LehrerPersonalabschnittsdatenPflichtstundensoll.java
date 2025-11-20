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
public final class ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}


	@Override
	protected boolean pruefe() {
		boolean success = true;
		final Double pflichtstundensoll = daten.pflichtstundensoll;
		final LehrerEinsatzstatus einsatzstatus = LehrerEinsatzstatus.getBySchluessel(daten.einsatzstatus);

		if (!exec(2, () -> (einsatzstatus == LehrerEinsatzstatus.B) && (pflichtstundensoll == 0.0),
				"Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'B'), darf das Pflichtstundensoll"
						+ " nicht 0,00 betragen."))
			success = false;

		return success;
	}

}
