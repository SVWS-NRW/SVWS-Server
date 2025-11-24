package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Die Lehrer-Personalabschnittdaten */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull DateManager geburtsdatum;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten          die Personalabschnittsdaten für den Validator
	 * @param geburtsdatum   das Geburtsdatum des Lehrers
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
		_validatoren.add(new ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}

}
