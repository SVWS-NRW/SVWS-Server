package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsvSchuelerStammdatenVorname extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vorname   der Vorname des Schuelers
	 * @param kontext   der Kontext der Schule
	 */
	public ValidatorSsvSchuelerStammdatenVorname(final @NotNull Supplier<@AllowNull String> vorname,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsv00SchuelerStammdatenVorname(vorname, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
