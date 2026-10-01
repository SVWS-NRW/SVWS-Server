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

		this._validatoren.add(new ValidatorLss10LehrerStammdatenStaatsangehoerigkeitID(idStaatsangehoerigkeit, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Nationalitaeten nationalitaet = Nationalitaeten.data().getWertByIDOrNull(this._idStaatsangehoerigkeit.get());

		if (nationalitaet == null) {
			addFehler(0, "Das Feld 'Staatsangehörigkeit' muss zulässig sein. ");
			return false;
		}

		return true;
	}

}
