package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der angegebene weitere Förderschwerpunkt
 * ein gültiger und zulässiger Eintrag im Katalog ist.
 */
public final class ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt1;
	private final @NotNull Supplier<@AllowNull Long> _idFoerderschwerpunkt2;

	private static final @NotNull String FEHLERTEXT = "Weiterer Förderschwerpunkt des Schülers: Das Feld 'Weiterer Förderschwerpunkt' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Zulässigkeit des weiteren Förderschwerpunkts.
	 *
	 * @param idFoerderschwerpunkt1   die ID des ersten Förderschwerpunkts
	 * @param idFoerderschwerpunkt2   die ID des weiteren Förderschwerpunkts
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlfz01SchuelerLernabschnittsdatenFoerderschwerpunktZwei(
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idFoerderschwerpunkt1 = idFoerderschwerpunkt1;
		_idFoerderschwerpunkt2 = idFoerderschwerpunkt2;

		_validatoren.add(new ValidatorSlfz10SchuelerLernabschnittsdatenFoerderschwerpunktZwei(idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idFoerderschwerpunkt2 = _idFoerderschwerpunkt2.get();

		// Ist das Feld leer, ist alles in Ordnung (es gibt keine Pflichtfeldprüfung 00)
		if (idFoerderschwerpunkt2 == null) {
			return true;
		}

		// Prüfe, ob die ID im Core-Type-Katalog für Förderschwerpunkte existiert
		if (Foerderschwerpunkt.data().getWertByIDOrNull(idFoerderschwerpunkt2) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
