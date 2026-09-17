package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der weitere Förderschwerpunkt nicht befüllt ist,
 * wenn der erste Förderschwerpunkt leer ist.
 */
public final class ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt1;
	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt2;

	private static final @NotNull String FEHLERTEXT =
			"Weiterer Förderschwerpunkt des Schülers: Das Feld 'Weiterer Förderschwerpunkt' darf nicht befüllt sein, wenn das Feld 'Förderschwerpunkt' leer ist.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Abhängigkeit vom ersten Förderschwerpunkt.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		_idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idFoerderschwerpunkt1 = _idFoerderschwerpunkt1.get();
		final @AllowNull Long idFoerderschwerpunkt2 = _idFoerderschwerpunkt2.get();

		if ((idFoerderschwerpunkt1 == null) && (idFoerderschwerpunkt2 != null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
