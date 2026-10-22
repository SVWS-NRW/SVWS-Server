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
public final class ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

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
	public ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
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

		// Bestimme das Rechtsverhältnis. Ist dieses nicht angegeben, so wird im Folgenden von einem sonstigen Rechtsverhältnis ausgegangen
		final LehrerRechtsverhaeltnis rv = _rechtsverhaeltnis.get();

		// Prüfe das Geburtsdatum bzw. das Alter bei den folgenden Rechtsverhältnissen...
		if (!rv.equals(LehrerRechtsverhaeltnis.L) && !rv.equals(LehrerRechtsverhaeltnis.P) && !rv.equals(LehrerRechtsverhaeltnis.W)) {
			// Sonstiges Rechtsverhältnis
			final int minJahr = schuljahr - 80;   // das erste akzeptierte Geburtsjahr: vor 80 Jahren
			final int maxJahr = schuljahr - 18;   // das letzte akzeptierte Geburtsjahr: vor 18 Jahren

			if (!_geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				addFehler(4, "Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen"
						+ " zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

}
