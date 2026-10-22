package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die 2. Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */

public final class ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/** Das aktuelle Schuljahr des Schülers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die zweite Staatsangehoerigkeit des Schuelers */
	private final @NotNull Supplier<Long> _idStaatsangehoerigkeit2;

	private static final @NotNull String FEHLERTEXT =
			"2. Staatsangehörigkeit des Schülers: Der eingetragene Wert für das Feld '2. Staatsangehörigkeit' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param schuljahr                 Schuljahr
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2ID
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idStaatsangehoerigkeit2,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_schuljahr = schuljahr;
		_idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;

	}

	@Override
	protected boolean pruefe() {
		if (!Nationalitaeten.data().isGueltig(_idStaatsangehoerigkeit2.get(), _schuljahr.get())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
