package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

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
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLssLehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_validatoren.add(new ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(idStaatsangehoerigkeit, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
