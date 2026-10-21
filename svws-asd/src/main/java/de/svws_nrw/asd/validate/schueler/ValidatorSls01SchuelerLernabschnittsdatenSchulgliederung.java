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
public final class ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung extends Validator {

	/** Die ID der Schulgliederung des Schuelers */
	private final @NotNull Supplier<@NotNull Long> _idSchulgliederung;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param schuljahr            Schuljahr
	 * @param idSchulgliederung    die ID der Schulgliederung
	 * @param kontext              der Kontext des Validators
	 */
	public ValidatorSls01SchuelerLernabschnittsdatenSchulgliederung(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);
		_idSchulgliederung = idSchulgliederung;

		_validatoren.add(new ValidatorSls02SchuelerLernabschnittsdatenSchulgliederung(schuljahr, idSchulgliederung, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Schulgliederung idSchulgliederung = Schulgliederung.data().getWertByIDOrNull(_idSchulgliederung.get());

		if (idSchulgliederung == null) {
			this.addFehler(0, "Schulgliederung des Schülers: Das Feld 'Schulgliederung' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
