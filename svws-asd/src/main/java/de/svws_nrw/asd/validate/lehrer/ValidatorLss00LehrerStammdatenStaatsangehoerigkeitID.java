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
public final class ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idStaatsangehoerigkeit;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_idStaatsangehoerigkeit = idStaatsangehoerigkeit;

		_validatoren.add(new ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(getNotNullSupplierLong(idStaatsangehoerigkeit),
				kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long staatsangehoerigkeitID = _idStaatsangehoerigkeit.get();

		if (staatsangehoerigkeitID == null) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
