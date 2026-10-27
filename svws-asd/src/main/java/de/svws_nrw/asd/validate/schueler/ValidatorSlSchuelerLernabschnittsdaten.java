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
	 * @param idOrganisationsform             die ID der Organisationsform
	 * @param idSchulgliederung               die ID der Schulgliederung
	 * @param idFachklasse                    die ID der Fachklasse
	 * @param hatSchwerbehinderungsNachweis   der Schwerbehinderungsnachweis
	 * @param idFoerderschwerpunkt1           die ID des ersten Förderschwerpunktes
	 * @param idFoerderschwerpunkt2           die ID des zweiten Förderschwerpunktes
	 * @param idSchuljahresabschnitt          die ID des Schuljahresabschnitts
	 * @param idKlassenart                    die Klassenart ID
	 * @param idEpJahre                       die EPJahre ID
	 * @param kontext                         der Kontext des Validators
	 */
	public ValidatorSlSchuelerLernabschnittsdaten(
			final @NotNull Supplier<@AllowNull Long> idOrganisationsform,
			final @NotNull Supplier<@AllowNull Long> idSchulgliederung,
			final @NotNull Supplier<@AllowNull Long> idFachklasse,
			final @NotNull Supplier<@AllowNull Boolean> hatSchwerbehinderungsNachweis,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt1,
			final @NotNull Supplier<@AllowNull Long> idFoerderschwerpunkt2,
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull Supplier<@AllowNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorSlbSchuelerLernabschnittsdatenFachklassen(idSchuljahresabschnitt, idFachklasse, kontext));
		_validatoren.add(new ValidatorSleSchuelerLernabschnittsdatenEpJahre(idSchuljahresabschnitt, idEpJahre, kontext));
		_validatoren.add(new ValidatorSlfSchuelerLernabschnittsdatenFoerderschwerpunkt(hatSchwerbehinderungsNachweis, idFoerderschwerpunkt1, idFoerderschwerpunkt2, kontext));
		_validatoren.add(new ValidatorSlkSchuelerLernabschnittsdatenKlassenart(idSchuljahresabschnitt, idKlassenart, kontext));
		_validatoren.add(new ValidatorSloaSchuelerLernabschnittsdatenOrganisationsformAllgemeinbildend(idSchuljahresabschnitt, idOrganisationsform, kontext));
		_validatoren.add(new ValidatorSlobSchuelerLernabschnittsdatenOrganisationsformBerufsbildend(idSchuljahresabschnitt, idOrganisationsform, kontext));
		_validatoren.add(new ValidatorSlowSchuelerLernabschnittsdatenOrganisationsformWeiterbildung(idSchuljahresabschnitt, idOrganisationsform, kontext));
		_validatoren.add(new ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung(idSchuljahresabschnitt, idSchulgliederung, kontext));
	}


	@Override
	protected boolean pruefe() {
		return true;
	}
}
