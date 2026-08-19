package de.svws_nrw.asd.validate.lehrer;

import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' die Lehrbefähigung 'Betreuung' voliegt.
 */
public final class ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _Lehrbefaehigung;

	/** Lehramt */
	private final @NotNull Supplier<@AllowNull LehrerLehramt> _LehrerLehramt;

	private static final @NotNull Set<LehrerLehramt> zulaessigeLehraemter = Set.of(LehrerLehramt.ID_63, LehrerLehramt.ID_64, LehrerLehramt.ID_65);

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehrbefaehigung     eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> lehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_Lehrbefaehigung = lehrbefaehigung;
		_LehrerLehramt = lehrerLehramt;
	}

	@Override
	protected boolean pruefe() {

			if (zulaessigeLehraemter.contains(_LehrerLehramt.get())) {

				if (!LehrerLehrbefaehigung.BE.equals(_Lehrbefaehigung.get())) {
					addFehler(0,
							"Für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' ist nur die Lehrbefähigung 'Betreuung' zulässig.");
					return false;
				}
			}
		return true;
	}
}
