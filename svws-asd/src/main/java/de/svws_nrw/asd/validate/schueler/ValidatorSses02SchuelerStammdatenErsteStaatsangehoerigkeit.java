package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die 1. Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */

public final class ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	/** Die Staatsangehoerigkeit des Schuelers */
	private final @NotNull Supplier<Long> _idStaatsangehoerigkeit;

	private static final @NotNull String FEHLERTEXT =
			"1. Staatsangehörigkeit des Schülers: Der eingetragene Wert für das Feld '1. Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param idStaatsangehoerigkeit   StaatsangehörigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit(
			final @NotNull Supplier<@NotNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idStaatsangehoerigkeit = idStaatsangehoerigkeit;

	}

	@Override
	protected boolean pruefe() {
		if (!Nationalitaeten.data().isGueltig(_idStaatsangehoerigkeit.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
