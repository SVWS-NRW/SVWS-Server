package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@AllowNull Long> _idLehrbefaehigung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idLehrbefaehigung   eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla00LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@AllowNull Long> idLehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idLehrbefaehigung = idLehrbefaehigung;
		_validatoren.add(new ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(getNotNullSupplierLong(idLehrbefaehigung), lehrerLehramt, kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long lehrbefaehigungID = _idLehrbefaehigung.get();

		if (lehrbefaehigungID == null) {
			addFehler(0, "Das Feld 'Lehrbefähigungen' muss besetzt sein.");
			return false;
		}

		return true;
	}

}
