package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum im Kontext des Rechtsverhältnisses
 * der Abschnittsdaten eines Lehrers einer Schule aus.
 */
public final class ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/** Das Geburtsdatum des Lehrers */
	private final @NotNull Supplier<@AllowNull DateManager> _geburtsdatum;

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Das Rechtsverhältnis */
	private final @NotNull Supplier<@NotNull LehrerRechtsverhaeltnis> _rechtsverhaeltnis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                  Schuljahr
	 * @param rechtsverhaeltnisNotNull   das Rechtsverhältnis
	 * @param geburtsdatum               das Geburtsdatum des Lehrers
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorLppr12LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull LehrerRechtsverhaeltnis> rechtsverhaeltnisNotNull,
			final @NotNull Supplier<@AllowNull DateManager> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_schuljahr = schuljahr;
		_rechtsverhaeltnis = rechtsverhaeltnisNotNull;
		_geburtsdatum = geburtsdatum;
	}

	@Override
	protected boolean pruefe() {
		final int schuljahr = _schuljahr.get();

		final DateManager geburtsdatumDateManager = _geburtsdatum.get();

		// ohne datum keine Prüfung möglich
		if (geburtsdatumDateManager != null) {

			// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
			if (LehrerRechtsverhaeltnis.W.equals(_rechtsverhaeltnis.get())) {
				// Beamtet auf Widerruf
				final int minJahr = schuljahr - 50; // das erste akzeptierte Geburtsjahr: vor 50 Jahren
				final int maxJahr = schuljahr - 18; // das letzte akzeptierte Geburtsjahr: vor 18 Jahren

				if (!geburtsdatumDateManager.istInJahren(minJahr, maxJahr)) {
					addFehler(3, "Der Wert für das Geburtsjahr sollte bei Lehramtsanwärtern/-innen (Rechtsverhältnis = W)"
							+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
					return false;
				}
			}
		}
		return true;
	}

}
