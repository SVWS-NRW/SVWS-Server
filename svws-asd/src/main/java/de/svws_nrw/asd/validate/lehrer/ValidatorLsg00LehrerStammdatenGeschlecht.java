package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsg00LehrerStammdatenGeschlecht extends Validator {

	/** Das Geschlecht des Lehrers */
	private final @NotNull Supplier<@AllowNull Integer> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsg00LehrerStammdatenGeschlecht(final @NotNull Supplier<@AllowNull Integer> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsg01LehrerStammdatenGeschlecht(getNotNullSupplierInteger(daten), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Integer geschlecht = daten.get();

		if (geschlecht == null) {
			addFehler(0, "Das Feld 'Geschlecht' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
