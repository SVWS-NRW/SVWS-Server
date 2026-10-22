package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Fachklassen bei den Lernabschnittsdaten
 * eines Schülers einer Schule aus.
 */
public final class ValidatorSlb00SchuelerLernabschnittsdatenFachklassen extends Validator {

	/** Die Fachklassen-ID des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _fachklasseID;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public ValidatorSlb00SchuelerLernabschnittsdatenFachklassen(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> fachklasseID,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		_fachklasseID = fachklasseID;

		_validatoren.add(new ValidatorSlb01SchuelerLernabschnittsdatenFachklassen(schuljahr, getNotNullSupplierLong(fachklasseID), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long fachklasseID = _fachklasseID.get();

		if (fachklasseID == null) {
			addFehler(0, "Fachklasse des Schülers: Das Feld  'Fachklasse' darf nicht leer sein.");
			return false;
		}

		return true;
	}

}
