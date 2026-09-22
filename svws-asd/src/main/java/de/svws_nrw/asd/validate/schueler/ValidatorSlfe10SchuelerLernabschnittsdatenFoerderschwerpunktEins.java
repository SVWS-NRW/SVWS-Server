package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der erste und zweite Förderschwerpunkt identisch sind.
 */
public final class ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt1;
	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt2;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(
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

		if ((idFoerderschwerpunkt1 == null) || (idFoerderschwerpunkt2 == null)) {
			return true;
		}

		if (idFoerderschwerpunkt1.equals(idFoerderschwerpunkt2)) {
			addFehler(0, "Förderschwerpunkt des Schülers: Der erste und der zweite Förderschwerpunkt dürfen nicht identisch sein.");
			return false;
		}

		return true;
	}

}
