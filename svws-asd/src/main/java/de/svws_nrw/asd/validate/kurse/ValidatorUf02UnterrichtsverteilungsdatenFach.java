package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob das Fach zulässig, bzw. in der JSON-Datei für das jeweilige Jahr vorhanden ist.
 */

public final class ValidatorUf02UnterrichtsverteilungsdatenFach extends Validator {

	/** Fach */
	private final @NotNull Supplier<@NotNull Long> _idFach;

	private static final @NotNull String FEHLERTEXT =
			"Fach des Kurses: Der eingetragene Wert für das Feld 'Fach' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public ValidatorUf02UnterrichtsverteilungsdatenFach(
			final @NotNull Supplier<@NotNull Long> idFach,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFach = idFach;

	}

	@Override
	protected boolean pruefe() {
		if (!Fach.data().isGueltig(_idFach.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
