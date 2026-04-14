package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geschlecht bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsg00SchuelerStammdatenGeschlecht extends Validator {

	/** Das Geschlecht des Schuelers */
	private final @NotNull Supplier<@AllowNull Integer> _idGeschlecht;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeschlecht     das Geschlecht des Schuelers
	 * @param kontext        der Kontext des Validators
	 */
	public ValidatorSsg00SchuelerStammdatenGeschlecht(final @NotNull Supplier<@AllowNull Integer> idGeschlecht, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idGeschlecht = idGeschlecht;
		_validatoren.add(new ValidatorSsg01SchuelerStammdatenGeschlecht(getNotNullSupplierInteger(idGeschlecht), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Integer idGeschlecht = _idGeschlecht.get();

		if (idGeschlecht == null) {
			addFehler(0, "Das Feld 'Geschlecht' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
