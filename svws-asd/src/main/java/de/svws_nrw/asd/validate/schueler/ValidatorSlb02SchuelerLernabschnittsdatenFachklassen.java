package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Fachklassen bei den Lernabschnittsdaten
 * eines Schuelers einer Schule aus.
 */

public final class ValidatorSlb02SchuelerLernabschnittsdatenFachklassen extends Validator {

	/** Das aktuelle Schuljahr des Schülers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Die FachklassenID des Schuelers */
	private final @NotNull Supplier<Long> _fachklasseID;

	private static final @NotNull String FEHLERTEXT =
			"Fachklasse des Schülers: Der eingetragene Wert für das Feld 'Fachklasse' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Fachklasse.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public ValidatorSlb02SchuelerLernabschnittsdatenFachklassen(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> fachklasseID,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_schuljahr = schuljahr;
		_fachklasseID = fachklasseID;
	}

	@Override
	protected boolean pruefe() {

		if (!Fachklasse.data().isGueltig(_fachklasseID.get(), _schuljahr.get())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
