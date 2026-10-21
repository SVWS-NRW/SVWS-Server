package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Schulgliederung bei den Lernabschnittsdaten
 * eines Schülers einer Schule aus.
 */
public final class ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/** Die ID der Schulgliederung des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _idSchulgliederung;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		_idSchulgliederung = idSchulgliederung;

		_validatoren.add(new ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung(schuljahr, getNotNullSupplierLong(idSchulgliederung), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idSchulgliederung = _idSchulgliederung.get();

		if (idSchulgliederung == null) {
			addFehler(0, "Schulgliederung des Schülers: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
