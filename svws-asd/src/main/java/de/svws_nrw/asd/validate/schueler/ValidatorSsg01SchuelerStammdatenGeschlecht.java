package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsg01SchuelerStammdatenGeschlecht extends Validator {

	/** Die Schueler-Stammdaten */
	private final @NotNull Supplier<Integer> _idGeschlecht;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht       das Geschlecht des Schuelers
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorSsg01SchuelerStammdatenGeschlecht(final @NotNull Supplier<Integer> idGeschlecht,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idGeschlecht = idGeschlecht;
	}

	@Override
	protected boolean pruefe() {
		final Geschlecht geschlecht = Geschlecht.fromValue(_idGeschlecht.get());

		if (geschlecht == null) {
			this.addFehler(0, "Unzulässiger Schlüssel '" + _idGeschlecht.get() + "' im Feld 'Geschlecht'. Die gültigen Schlüssel entnehmen Sie bitte dem Pulldownmenü.");
			return false;
		}

		return true;
	}

}
