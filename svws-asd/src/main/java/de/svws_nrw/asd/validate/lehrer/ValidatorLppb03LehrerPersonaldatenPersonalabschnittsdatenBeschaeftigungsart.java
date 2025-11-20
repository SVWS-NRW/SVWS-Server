package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLppb03LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}


	@Override
	protected boolean pruefe() {
		boolean success = true;
		final String beschaeftigungsart = daten.beschaeftigungsart;
		final String einsatzstatus = daten.einsatzstatus;

		// LPPB3 ex BW15
		final Double pflichtstundensoll = daten.pflichtstundensoll;
		final String fehlertext3 = "Laut Ihren Angaben handelt es sich um eine voll abgeordnete Lehrkraft mit Gestellungsvertrag. Es ist zu erwarten, "
				+ "dass eine Lehrkraft mit Gestellungsvertrag Unterricht an Ihrer Schule erteilt. Bitte überprüfen Sie Ihre Angaben.";
		if (!exec(3, () -> ("G".equals(beschaeftigungsart) && "A".equals(einsatzstatus) && pflichtstundensoll == 0), fehlertext3))
			success = false;

		return success;
	}

}
