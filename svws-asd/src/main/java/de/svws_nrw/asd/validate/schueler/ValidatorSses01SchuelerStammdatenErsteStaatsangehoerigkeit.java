package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die 1. Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	/** Die Staatsangehoerigkeit des Schuelers */
	private final @NotNull Supplier<Long> _idStaatsangehoerigkeit;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit   StaatsangehoerigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorSses01SchuelerStammdatenErsteStaatsangehoerigkeit(final @NotNull Supplier<Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		_validatoren.add(new ValidatorSses02SchuelerStammdatenErsteStaatsangehoerigkeit(idStaatsangehoerigkeit, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Nationalitaeten sa = Nationalitaeten.data().getWertByIDOrNull(_idStaatsangehoerigkeit.get());

		if (sa == null) {
			this.addFehler(0, "1. Staatsangehörigkeit des Schülers: Das Feld '1. Staatsangehörigkeit' muss zulässig sein.");
			return false;
		}

		return true;
	}

}

