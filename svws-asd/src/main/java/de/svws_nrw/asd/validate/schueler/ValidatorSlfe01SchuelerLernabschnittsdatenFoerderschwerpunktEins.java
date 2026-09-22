package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der angegebene erste Förderschwerpunkt
 * ein gültiger und zulässiger Eintrag im Katalog ist.
 */
public final class ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt1;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des zweiten Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfe01SchuelerLernabschnittsdatenFoerderschwerpunktEins(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		_validatoren.add(new ValidatorSlfe10SchuelerLernabschnittsdatenFoerderschwerpunktEins(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idFoerderschwerpunkt1 = _idFoerderschwerpunkt1.get();

		// Da nicht jeder Schüler einen Förderschwerpunkt haben muss, ist null hier zulässig
		if (idFoerderschwerpunkt1 == null) {
			return true;
		}

		if (Foerderschwerpunkt.data().getWertByIDOrNull(idFoerderschwerpunkt1) == null) {
			addFehler(0, "Förderschwerpunkt des Schülers: Das Feld 'Förderschwerpunkt' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
