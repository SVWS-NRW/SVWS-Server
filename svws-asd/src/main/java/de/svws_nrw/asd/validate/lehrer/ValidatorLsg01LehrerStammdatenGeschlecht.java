package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsg01LehrerStammdatenGeschlecht extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<@AllowNull Integer> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsg01LehrerStammdatenGeschlecht(final @NotNull Supplier<@AllowNull Integer> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		Geschlecht geschlecht = null;
		geschlecht = Geschlecht.fromValue(daten.get());
		final Geschlecht finalGeschlecht = geschlecht;

		if (finalGeschlecht == null) {
			this.addFehler(0, "Unzulässiger Schlüssel '" + geschlecht + "' im Feld 'Geschlecht'. Die gültigen Schlüssel entnehmen Sie bitte dem Pulldownmenü.");
			return false;
		}

		return true;
	}

}
