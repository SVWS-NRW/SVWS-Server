package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob das Fach zulässig, bzw. in der JSON-Datei vorhanden ist.
 */
public final class ValidatorUf00UnterrichtsverteilungsdatenFach extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFach;
	private static final @NotNull String FEHLERTEXT = "Fach des Kurses: Kein Wert vorhanden.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public ValidatorUf00UnterrichtsverteilungsdatenFach(
			final @NotNull Supplier<@AllowNull Long> idFach,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFach = idFach;

		_validatoren.add(new ValidatorUf01UnterrichtsverteilungsdatenFach(getNotNullSupplierLong(idFach), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Bestimme Fach.
		final @AllowNull Long idFach = _idFach.get();

			if (idFach == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
		return true;
	}
}
