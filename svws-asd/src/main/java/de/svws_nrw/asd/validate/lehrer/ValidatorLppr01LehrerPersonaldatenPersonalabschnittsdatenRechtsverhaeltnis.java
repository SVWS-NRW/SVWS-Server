package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

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
	public ValidatorLppr01LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		this.geburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Schuljahr über den Schuljahresabschnitt. Treten dabei Fehler auf, so ist dieser durch einen übergeordneten Validator zu prüfen.
		final Schuljahresabschnitt schuljahresabschnitt = kontext().getSchuljahresabschnittByID(daten.idSchuljahresabschnitt);
		if (schuljahresabschnitt == null)
			return false;
		final int schuljahr = schuljahresabschnitt.schuljahr;

		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.getBySchluessel(daten.rechtsverhaeltnis);

		// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
		if (rv.equals(LehrerRechtsverhaeltnis.L))  {
			// Beamtet auf Lebenszeit
			/* Das erste akzeptierte Geburtsjahr variiert je nach Renteneintrittsalter:
			 * - Schuljahre vor 2022: vor 1958 - Rentenaltersgrenze 65 Jahre
			 * - Schuljahr 2023: Sonderfall - Definition Rentenaltersgrenze 65 Jahre
			 * - Schuljahre 2024-2029: vor 1964 - Rentenaltersgrenze 66 Jahre
			 * - Schuljahr 2030: Sonderfall - Definition Rentenaltersgrenze 66 Jahre
			 * - Schuljahre nach 2030: ab 1964 - Rentenaltersgrenze 67 Jahre
			 *  */
			final int minJahr = schuljahr - ((schuljahr <= 2023) ? 65 : ((schuljahr <= 2030) ? 66 : 67));
			final int maxJahr = schuljahr - 27;  // Das letzte akzeptierte Geburtsjahr: vor 27 Jahren

			if (!geburtsdatum.istInJahren(minJahr, maxJahr)) {
				this.addFehler(1, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L)"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
	}
	return true;
	}

}
