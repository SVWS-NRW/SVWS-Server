package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit extends Validator {

	/** Die Staatsangehoerigkeit2 des Schuelers */
	private final @NotNull Supplier<@AllowNull Long> _idStaatsangehoerigkeit2;
	/** Die Staatsangehoerigkeit des Schuelers */
	private final @NotNull Supplier<@AllowNull Long> _idStaatsangehoerigkeit;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2ID
	 * @param idStaatsangehoerigkeit    StaatsangehörigkeitID
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit(
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit2,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idStaatsangehoerigkeit2 = idStaatsangehoerigkeit2;
		this._idStaatsangehoerigkeit = idStaatsangehoerigkeit;
		_validatoren.add(new ValidatorSszs00SchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, idStaatsangehoerigkeit, kontext));
	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
