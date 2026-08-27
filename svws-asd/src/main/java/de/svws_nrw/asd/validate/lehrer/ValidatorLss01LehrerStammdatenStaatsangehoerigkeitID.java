package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die StaatsangehoerigkeitID bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idStaatsangehoerigkeit;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   die idStaatsangehoerigkeit des Lehrers
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLss01LehrerStammdatenStaatsangehoerigkeitID(final @NotNull Supplier<@NotNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;

		final @NotNull Supplier<@NotNull String> staatsangehoerigkeitSchluessel =
				getNotNullSupplier(() -> Nationalitaeten.data().getSchluesselByIDOrNull(_idStaatsangehoerigkeit.get()));
		// Schuljahr wird als Testparameter benötigt und daher hier separat übergeben.
		final @NotNull Supplier<Integer> schuljahr = () -> kontext.getSchuljahr();

		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(staatsangehoerigkeitSchluessel, schuljahr, kontext));
	}

	@Override
	protected boolean pruefe() {
		final String staatsangehoerigkeitSchluessel = Nationalitaeten.data().getSchluesselByIDOrNull(this._idStaatsangehoerigkeit.get());

		if (staatsangehoerigkeitSchluessel == null) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}

		return true;
	}

}
