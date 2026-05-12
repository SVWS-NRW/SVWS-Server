package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Die Lehrer-Stammdaten */
	private final @NotNull Supplier<String> _staatsangehoerigkeitID;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitID   die StaatsangehoerigkeitID des Lehrers
	 * @param rechtsverhaeltnis        das Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<String> staatsangehoerigkeitID,
			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_staatsangehoerigkeitID = staatsangehoerigkeitID;
		_validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitID, kontext));
		_validatoren.add(new ValidatorLss11LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitID, rechtsverhaeltnis, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Nationalitaeten staatsangehoerigkeitID = Nationalitaeten.getByDESTATIS(_staatsangehoerigkeitID.get());

		if (staatsangehoerigkeitID == null) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}

		return true;
	}

}
