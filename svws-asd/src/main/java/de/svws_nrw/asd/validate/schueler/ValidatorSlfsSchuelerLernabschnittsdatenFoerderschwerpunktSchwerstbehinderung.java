package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den ersten Förderschwerpunkt-Schwerstbehinderung
 * der Lernabschnittsdaten eines Schülers aus.
 */
public final class ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param kontext                         der Kontext des Validators
	 */
	public ValidatorSlfsSchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Boolean> hatSchwerbehinderungsNachweis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(idFoerderschwerpunkt1, hatSchwerbehinderungsNachweis, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
