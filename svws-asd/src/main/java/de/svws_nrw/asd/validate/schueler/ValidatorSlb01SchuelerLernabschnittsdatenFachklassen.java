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
public final class ValidatorSlb01SchuelerLernabschnittsdatenFachklassen extends Validator {

	/** Die FachklassenID des Schuelers */
	private final @NotNull Supplier<@NotNull Long> _fachklasseID;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Fachklasse.
	 *
	 * @param schuljahr       Schuljahr
	 * @param fachklasseID    FachklassenID
	 * @param kontext         der Kontext des Validators
	 */
	public ValidatorSlb01SchuelerLernabschnittsdatenFachklassen(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> fachklasseID,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		_fachklasseID = fachklasseID;

		_validatoren.add(new ValidatorSlb02SchuelerLernabschnittsdatenFachklassen(schuljahr, fachklasseID, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Fachklasse fk = Fachklasse.data().getWertByIDOrNull(_fachklasseID.get());

		if (fk == null) {
			this.addFehler(0, "Fachklasse des Schülers: Das Feld 'Fachklasse' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
