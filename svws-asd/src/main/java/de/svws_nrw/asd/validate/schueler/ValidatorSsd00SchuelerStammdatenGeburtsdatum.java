package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsdatum bei den Stammdaten
 * eines Schülers aus.
 */
public final class ValidatorSsd00SchuelerStammdatenGeburtsdatum extends Validator {

	/** Das Geburtsdatum des Schülers */
	private final @NotNull Supplier<@AllowNull String> fieldGeburtsdatum;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geburtsdatum     das Geburtsdatum des Schülers
	 * @param kontext          der Kontext des Validators
	 */
	public ValidatorSsd00SchuelerStammdatenGeburtsdatum(final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.fieldGeburtsdatum = geburtsdatum;
		_validatoren.add(new ValidatorSsd01SchuelerStammdatenGeburtsdatum(getNotNullSupplier(geburtsdatum), kontext));
	}

	@Override
	protected boolean pruefe() {
		final String geburtsdatum = this.fieldGeburtsdatum.get();

		if ((geburtsdatum == null) || (geburtsdatum.isEmpty())) {
			addFehler(0, "Das Feld 'Geburtsdatum' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
