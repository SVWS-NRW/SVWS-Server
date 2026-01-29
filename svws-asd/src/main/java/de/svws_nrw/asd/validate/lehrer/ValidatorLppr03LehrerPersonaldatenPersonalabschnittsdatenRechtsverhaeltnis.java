package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

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
public final class ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull Supplier<DateManager> geburtsdatum;

	/** Die ID des Schuljahresabschnittes */
	private final @NotNull Supplier<Long> idSchuljahresabschnitt;

	/** Das Rechtsverhältnis */
	private final @NotNull Supplier<String> rechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis       das Rechtsverhältnis
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorLppr03LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<Long> idSchuljahresabschnitt,
			final @NotNull Supplier<String> rechtsverhaeltnis,
			final @NotNull Supplier<DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
		this.geburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {
		// Bestimme das Schuljahr über den Schuljahresabschnitt. Treten dabei Fehler auf, so ist dieser durch einen übergeordneten Validator zu prüfen.
		final Schuljahresabschnitt schuljahresabschnitt = kontext().getSchuljahresabschnittByID(this.idSchuljahresabschnitt.get());
		if (schuljahresabschnitt == null)
			return false;
		final int schuljahr = schuljahresabschnitt.schuljahr;

		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final LehrerRechtsverhaeltnis rv = LehrerRechtsverhaeltnis.getBySchluessel(this.rechtsverhaeltnis.get());

		// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
		if (rv.equals(LehrerRechtsverhaeltnis.W))  {
			// Beamtet auf Widerruf
			final int minJahr = schuljahr - 50; // das erste akzeptierte Geburtsjahr: vor 50 Jahren
			final int maxJahr = schuljahr - 18; // das letzte akzeptierte Geburtsjahr: vor 18 Jahren

			if (!geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				this.addFehler(3, "Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W)"
						+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
	}
	return true;
	}

}
