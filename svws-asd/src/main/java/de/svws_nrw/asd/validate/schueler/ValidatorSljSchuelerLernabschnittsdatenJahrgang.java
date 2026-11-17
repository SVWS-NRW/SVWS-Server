package de.svws_nrw.asd.validate.schueler;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Jahrgang
 * der Lernabschnittsdaten eines Schülers einer Schule aus.
 */
public final class ValidatorSljSchuelerLernabschnittsdatenJahrgang extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param jahrgangID         die ID des Jahrgangs des Schülers
	 * @param idListJahrgaenge   die Liste aller Jahrgänge der Schule
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorSljSchuelerLernabschnittsdatenJahrgang(
			final @NotNull Supplier<@AllowNull Long> jahrgangID,
			final @NotNull Supplier<List<Long>> idListJahrgaenge,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSlj00SchuelerLernabschnittsdatenJahrgang(jahrgangID, idListJahrgaenge, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
