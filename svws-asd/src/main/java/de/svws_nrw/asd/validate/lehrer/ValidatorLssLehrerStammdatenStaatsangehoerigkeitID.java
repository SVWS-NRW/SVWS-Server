package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehörigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLssLehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitID   die StaatsangehörigkeitID des Lehrers
	 * @param idRechtsverhaeltnis      die ID des Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLssLehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@AllowNull String> staatsangehoerigkeitID,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis =
				() -> LehrerRechtsverhaeltnis.data().getWertByIDOrNull(idRechtsverhaeltnis.get());
		_validatoren.add(new ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitID, rechtsverhaeltnis, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
