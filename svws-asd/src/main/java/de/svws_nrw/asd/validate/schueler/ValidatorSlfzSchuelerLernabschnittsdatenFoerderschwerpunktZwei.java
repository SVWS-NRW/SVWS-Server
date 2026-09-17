package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den weiteren Förderschwerpunkt
 * der Lernabschnittsdaten eines Schülers aus.
 */
public final class ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfzSchuelerLernabschnittsdatenFoerderschwerpunktZwei(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
