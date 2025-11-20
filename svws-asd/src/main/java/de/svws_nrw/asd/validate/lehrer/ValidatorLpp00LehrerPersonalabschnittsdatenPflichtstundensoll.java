package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLpp00LehrerPersonalabschnittsdatenPflichtstundensoll(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLpp01LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		_validatoren.add(new ValidatorLpp02LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		_validatoren.add(new ValidatorLpp03LehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
	}


	@Override
	protected boolean pruefe() {
		boolean success = true;
		final Double pflichtstundensoll = daten.pflichtstundensoll;

		success = exec(0, () -> pflichtstundensoll == null, "Kein Wert im Feld 'pflichtstundensoll'.");
		if (!success)
			return false;

		return true;
	}

}
