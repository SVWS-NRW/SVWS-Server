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
public final class ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<@AllowNull String> _staatsangehoerigkeitID;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param staatsangehoerigkeitID   die StaatsangehörigkeitID des Lehrers
	 * @param rechtsverhaeltnis      das Rechtsverhältnis des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@AllowNull String> staatsangehoerigkeitID,
			final @NotNull Supplier<@AllowNull LehrerRechtsverhaeltnis> rechtsverhaeltnis, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_staatsangehoerigkeitID = staatsangehoerigkeitID;
		_validatoren.add(new ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(getNotNullSupplier(staatsangehoerigkeitID),
				rechtsverhaeltnis, kontext));
	}

	@Override
	protected boolean pruefe() {
		final String staatsangehoerigkeitID = _staatsangehoerigkeitID.get();

		if ((staatsangehoerigkeitID == null) || staatsangehoerigkeitID.isEmpty()) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
