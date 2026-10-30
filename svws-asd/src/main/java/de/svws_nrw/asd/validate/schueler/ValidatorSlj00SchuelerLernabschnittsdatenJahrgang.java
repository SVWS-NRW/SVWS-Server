package de.svws_nrw.asd.validate.schueler;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.JahrgaengeStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Feld 'Jahrgang' in den Lernabschnittsdaten
 * eines Schülers besetzt ist.
 */
public final class ValidatorSlj00SchuelerLernabschnittsdatenJahrgang extends Validator {

	/** Die ID des Jahrgangs des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _jahrgangID;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param jahrgangID   die ID des Jahrgangs des Schülers
	 * @param jahrgaenge   die Liste aller Jahrgänge der Schule
	 * @param kontext      der Kontext des Validators
	 */
	public ValidatorSlj00SchuelerLernabschnittsdatenJahrgang(
			final @NotNull Supplier<@AllowNull Long> jahrgangID,
			final @NotNull Supplier<List<JahrgaengeStatistikGesamt>> jahrgaenge,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_jahrgangID = jahrgangID;
		_validatoren.add(new ValidatorSlj01SchuelerLernabschnittsdatenJahrgang(jahrgangID, jahrgaenge, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long jahrgangID = _jahrgangID.get();

		if (jahrgangID == null) {
			addFehler(0, "Jahrgang des Schülers: Das Feld 'Jahrgang' darf nicht leer sein");
			return false;
		}

		return true;
	}

}
