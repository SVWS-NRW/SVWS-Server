package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

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
	public ValidatorLppr00LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
		_validatoren.add(new ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr02LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		_validatoren.add(new ValidatorLppr04LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));

	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.getBySchluessel(daten.rechtsverhaeltnis);

		if (rv == null) {
			this.addFehler(0, "Kein Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}

		return true;
	}

}
