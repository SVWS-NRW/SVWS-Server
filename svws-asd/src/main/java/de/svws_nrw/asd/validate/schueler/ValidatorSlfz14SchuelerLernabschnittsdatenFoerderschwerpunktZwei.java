package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob bei dem ersten Förderschwerpunkt 'Klinikschule' (KR)
 * ein weiterer Förderschwerpunkt angegeben wurde.
 */
public final class ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt1;
	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt2;

	private static final @NotNull String FEHLERTEXT =
			"Förderschwerpunkt des Schülers: Der angegebene Förderschwerpunkt 'Klinikschule' ist nicht in Kombination mit anderen Förderschwerpunkten zulässig.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Förderschwerpunkts Klinikschule (KR)
	 * in Kombination mit einem weiteren Förderschwerpunkt.
	 *
	 * @param idFoerderschwerpunkt1   die Katalog-ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die Katalog-ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfz14SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		_idFoerderschwerpunkt2 = idFoerderschwerpunkt2;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long id1 = _idFoerderschwerpunkt1.get();
		final @AllowNull Long id2 = _idFoerderschwerpunkt2.get();

		if ((id1 == null) || (id2 == null)) {
			return true;
		}

		final @AllowNull Foerderschwerpunkt fs1 = Foerderschwerpunkt.data().getWertByIDOrNull(id1);

		if (fs1 == Foerderschwerpunkt.KR) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
