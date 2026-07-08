package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Lehrbefähigung zulässig, bzw. in der JSON-Datei vorhanden ist.
 */
public final class ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull Long> _idLehrbefaehigung;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param idLehrbefaehigung   eine idLehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@NotNull Long> idLehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idLehrbefaehigung = idLehrbefaehigung;

		_validatoren.add(new ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(idLehrbefaehigung, lehrerLehramt, kontext));
	}

	@Override
	protected boolean pruefe() {
		final String lehrbefaehigungSchluessel = LehrerLehrbefaehigung.data().getSchluesselByIDOrNull(this._idLehrbefaehigung.get());

		if (lehrbefaehigungSchluessel == null) {
			addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
			return false;
		}

		return true;
	}
}
