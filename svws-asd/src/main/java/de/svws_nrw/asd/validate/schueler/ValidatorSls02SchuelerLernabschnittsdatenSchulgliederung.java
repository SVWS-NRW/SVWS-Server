package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Schulgliederung bei den Lernabschnittsdaten
 * eines Schuelers einer Schule aus.
 */

public final class ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/** Das aktuelle Schuljahr des Schülers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die ID der Schulgliederung des Schuelers */
	private final @NotNull Supplier<@NotNull Long> _idSchulgliederung;

	private static final @NotNull String FEHLERTEXT =
			"Schulgliederung des Schülers: Der eingetragene Wert für das Feld 'Schulgliederung' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_schuljahr = schuljahr;
		_idSchulgliederung = idSchulgliederung;
	}

	@Override
	protected boolean pruefe() {

		if (!Schulgliederung.data().isGueltig(_idSchulgliederung.get(), _schuljahr.get())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
