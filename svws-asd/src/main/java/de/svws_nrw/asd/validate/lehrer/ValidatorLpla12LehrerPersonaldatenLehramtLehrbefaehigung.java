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
 * Prüft das Feld Lehrbefähigung.
 * Das Feld 'Lehrbefähigung' darf nur bei den Lehrämtern 'Berufsbildende Schulen - altes Lehramt -',
 * 'Sekundarstufe II (mit beruflicher Fachrichtung)' und 'Berufskolleg' leer sein.
 */
public final class ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _Lehrbefaehigung;

	/** Lehramt */
	private final @NotNull Supplier<@AllowNull LehrerLehramt> _lehrerLehramt;

	private static final @NotNull Set<LehrerLehramt> zulaessigeLehraemter = Set.of(LehrerLehramt.ID_30, LehrerLehramt.ID_32, LehrerLehramt.ID_35);

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehrbefaehigung     eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> lehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_Lehrbefaehigung = lehrbefaehigung;
		_lehrerLehramt = lehrerLehramt;
	}

	@Override
	protected boolean pruefe() {

		if (!zulaessigeLehraemter.contains(_lehrerLehramt.get())) {

			if (_Lehrbefaehigung.get() == null) {
				addFehler(0,
						"Das Feld 'Lehrbefähigung' darf nur bei den Lehrämtern 'Berufsbildende Schulen - altes Lehramt -', 'Sekundarstufe II (mit beruflicher Fachrichtung)' und 'Berufskolleg' leer sein.");
				return false;
			}
		}
		return true;
	}
}
