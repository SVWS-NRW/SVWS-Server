package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Klassenart eines Schülers aus.
 */
public final class ValidatorSlSchuelerLernabschnittsdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnitts
	 * @param idKlassenart            die Klassenart ID
	 * @param idEpJahre               die EPJahre ID
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorSlSchuelerLernabschnittsdaten(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull Supplier<@AllowNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(
				new ValidatorSlkSchuelerLernabschnittsdatenKlassenart(idSchuljahresabschnitt, idKlassenart, kontext));
		_validatoren.add(new ValidatorSleSchuelerLernabschnittsdatenEpJahre(idSchuljahresabschnitt, idEpJahre, kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}
}
