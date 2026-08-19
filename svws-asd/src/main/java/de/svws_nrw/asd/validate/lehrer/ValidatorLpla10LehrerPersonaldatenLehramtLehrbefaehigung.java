package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Lehrbefähigung korrekt besetzt ist.
 */
public final class ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _Lehrbefaehigung;

	/** Lehramt */
	private final @NotNull Supplier<@AllowNull LehrerLehramt> _lehrerLehramt;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param _lehrbefaehigung    eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _lehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_Lehrbefaehigung = _lehrbefaehigung;
		_lehrerLehramt = lehrerLehramt;
	}

	@Override
	protected boolean pruefe() {
			if (LehrerLehramt.ID_70.equals(_lehrerLehramt.get())) {
				if (!LehrerLehrbefaehigung.OA.equals(_Lehrbefaehigung.get())) {
					addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' ist nur die Lehrbefähigung 'ohne Angabe' zulässig.");
					return false;
				}
			}
		return true;
	}
}
