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
public final class ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1;
	private final @NotNull Supplier<@AllowNull Boolean> hatSchwerbehinderungsNachweis;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunkts
	 * @param hatSchwerbehinderungsNachweis   Schwerbehinderungsnachweis vorhanden
	 * @param kontext                         der Kontext des Validators
	 */
	public ValidatorSlfs10SchuelerLernabschnittsdatenFoerderschwerpunktSchwerstbehinderung(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Boolean> hatSchwerbehinderungsNachweis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		this.hatSchwerbehinderungsNachweis = hatSchwerbehinderungsNachweis;
	}

	@Override
	protected boolean pruefe() {
		final Long idFoerderschwerpunkt1 = this.idFoerderschwerpunkt1.get();
		final Boolean hatSchwerbehinderungsNachweis = this.hatSchwerbehinderungsNachweis.get();

		if (hatSchwerbehinderungsNachweis != null && hatSchwerbehinderungsNachweis && idFoerderschwerpunkt1 == null) {
			addFehler(0, "Wenn eine Schwerstbehinderung vorliegt, muss das Feld 'Förderschwerpunkt' ebenfalls besetzt sein.");
			return false;
		}

		return true;
	}

}
