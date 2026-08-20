package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob das Fach zulässig, bzw. in der JSON-Datei vorhanden ist.
 */

public final class ValidatorUf01UnterrichtsverteilungsdatenFach extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idFach;
	private static final @NotNull String FEHLERTEXT = "Fach des Kurses: Das Feld 'Fach' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public ValidatorUf01UnterrichtsverteilungsdatenFach(
			final @NotNull Supplier<@NotNull Long> idFach,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFach = idFach;

		_validatoren.add(
				new ValidatorUf02UnterrichtsverteilungsdatenFach(idFach, kontext));

	}

	@Override
	protected boolean pruefe() {
		final @NotNull Long idFach = _idFach.get();
		final @AllowNull Fach fach = Fach.data().getWertByIDOrNull(idFach);

		if (fach == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
