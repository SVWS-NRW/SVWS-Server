package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Einsatzstatus der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEinsatzstatus   die ID des Einsatzstatus.
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorLppeLehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(
				new ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(getNotNullSupplierLong(idEinsatzstatus),
						kontext));

	}


	@Override
	protected boolean pruefe() {

		return true;
	}
}
