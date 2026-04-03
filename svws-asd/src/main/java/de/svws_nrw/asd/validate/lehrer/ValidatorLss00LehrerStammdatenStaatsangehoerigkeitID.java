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

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten                 die StaatsangehörigkeitID des Lehrers
	 * @param idRechtsverhaeltnis   das Rechtsverhältnis des Lehrers
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLss00LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull Supplier<@AllowNull Long> idRechtsverhaeltnis, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(getNotNullSupplier(daten), getNotNullSupplierLong(idRechtsverhaeltnis), kontext));
	}

	@Override
	protected boolean pruefe() {
		final String staatsangehoerigkeitID = daten.get();

		if (staatsangehoerigkeitID == null || staatsangehoerigkeitID.isEmpty()) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
