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
public final class ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum extends Validator<LehrerPersonalabschnittsdaten> {

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull DateManager geburtsdatum;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten          die Personalabschnittsdaten für den Validator
	 * @param geburtsdatum   das Geburtsdatum des Lehrers
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnisGeburtsdatum(final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull DateManager geburtsdatum, final @NotNull ValidatorKontext kontext) {
		super(daten, kontext);
		this.geburtsdatum = geburtsdatum;
	}


	@Override
	protected boolean pruefe() {
		// Bestimme das Schuljahr über den Schuljahresabschnitt. Treten dabei Fehler auf, so ist dieser durch einen übergeordneten Validator zu prüfen.
		final Schuljahresabschnitt schuljahresabschnitt = kontext().getSchuljahresabschnittByID(daten().idSchuljahresabschnitt);
		if (schuljahresabschnitt == null)
			return false;
		final int schuljahr = schuljahresabschnitt.schuljahr;

		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.getBySchluessel(daten().rechtsverhaeltnis);
		if (rv == null) {
			addFehler("Kein Wert im Feld 'rechtsverhaeltnis'.");
			return false;
		}

		// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
		boolean success = true;
		switch (rv) {
			case LehrerRechtsverhaeltnis.L -> { // Beamtet auf Lebenszeit
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
					addFehler("Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L)"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
			}
			case LehrerRechtsverhaeltnis.P -> { // Beamtet auf Probe
				final int minJahr = schuljahr - 55; // das erste akzeptierte Geburtsjahr: vor 55 Jahren
				final int maxJahr = schuljahr - 20; // das letzte akzeptierte Geburtsjahr: vor 20 Jahren
				if (!geburtsdatum.istInJahren(minJahr, maxJahr)) {
					addFehler("Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Probe (Rechtsverhältnis = P)"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
			}
			case LehrerRechtsverhaeltnis.W -> { // Beamtet in der Lehramtsausbildung
				final int minJahr = schuljahr - 50; // das erste akzeptierte Geburtsjahr: vor 50 Jahren
				final int maxJahr = schuljahr - 18; // das letzte akzeptierte Geburtsjahr: vor 18 Jahren
				if (!geburtsdatum.istInJahren(minJahr, maxJahr)) {
					addFehler("Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W)"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
			}
			default -> { // Sonstiges Rechtsverhältnis
				final int minJahr = schuljahr - 80;   // das erste akzeptierte Geburtsjahr: vor 80 Jahren
				final int maxJahr = schuljahr - 18;   // das letzte akzeptierte Geburtsjahr: vor 18 Jahren
				if (!geburtsdatum.istInJahren(minJahr, maxJahr)) {
					addFehler("Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					success = false;
				}
			}
		}
		return success;
	}

}
