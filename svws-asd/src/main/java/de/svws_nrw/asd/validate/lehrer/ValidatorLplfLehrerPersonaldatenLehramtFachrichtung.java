package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * * Dieser Validator führt eine Statistikprüfung auf eine vorhandene Fachrichtung zu einem Lehramt eines Lehrers aus.
 */
public final class ValidatorLplfLehrerPersonaldatenLehramtFachrichtung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idFachrichtung        die Katalog-ID der Fachrichtung
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplfLehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung(idFachrichtung, kontext));

	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
