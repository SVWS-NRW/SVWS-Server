package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die 2. Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/** Die Staatsangehoerigkeit des Schuelers */
	private final @NotNull Supplier<Long> _idStaatsangehoerigkeit2;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit2   Staatsangehoerigkeit2ID
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSszs01SchuelerStammdatenZweiteStaatsangehoerigkeit(final @NotNull Supplier<Long> idStaatsangehoerigkeit2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;
		_validatoren.add(new ValidatorSszs02SchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Nationalitaeten sa = Nationalitaeten.data().getWertByIDOrNull(_idStaatsangehoerigkeit2.get());

		if (sa == null) {
			this.addFehler(0, "2. Staatsangehörigkeit des Schülers: Das Feld '2. Staatsangehörigkeit' muss zulässig sein.");
			return false;
		}

		return true;
	}

}

