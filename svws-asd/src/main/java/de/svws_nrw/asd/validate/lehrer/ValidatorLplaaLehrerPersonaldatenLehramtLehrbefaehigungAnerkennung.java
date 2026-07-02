package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(
			final @NotNull Supplier<Long> idAnerkennungsgrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(idAnerkennungsgrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
