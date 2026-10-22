package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull Supplier<DateManager> _geburtsdatum;

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Das Rechtsverhältnis */
	private final @NotNull Supplier<LehrerRechtsverhaeltnis> _rechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                  Schuljahr
	 * @param rechtsverhaeltnisNotNull   das Rechtsverhältnis
	 * @param geburtsdatum               das Geburtsdatum des Lehrers
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorLppr10LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<LehrerRechtsverhaeltnis> rechtsverhaeltnisNotNull,
			final @NotNull Supplier<DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schuljahr = schuljahr;
		_rechtsverhaeltnis = rechtsverhaeltnisNotNull;
		_geburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {
		final int schuljahr = _schuljahr.get();

		// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
		if (_rechtsverhaeltnis.get().equals(LehrerRechtsverhaeltnis.L)) {
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

			if (!_geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				addFehler(1, "Der Wert für das Geburtsjahr sollte bei Beamten/-innen auf Lebenszeit (Rechtsverhältnis = L)"
						+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

}
