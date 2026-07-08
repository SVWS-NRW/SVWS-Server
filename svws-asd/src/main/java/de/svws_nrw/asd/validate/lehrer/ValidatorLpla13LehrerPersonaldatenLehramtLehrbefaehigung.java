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
 * Bei den Lehrbefähigungen 'AE - Ästhetische Erziehung', 'MG - Mathematische Grundbildung', 'NG - Natur- und
 * Gesellschaftswissenschaften' und 'SB - Sprachliche Grundbildung' muss das Lehramt 'Grundschule' oder
 * 'Sonderpädagogische Förderung' bzw. die Lehramtseinträge 'Studierende' oder 'Lehramtsanwärter/-in / Studienreferendar/-in' angegeben werden.
 */
public final class ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Lehrbefähigung */
	private final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> _lehrbefaehigung;

	/** Lehramt */
	private final @NotNull Supplier<@AllowNull LehrerLehramt> _lehrerLehramt;

	private static final @NotNull Set<LehrerLehramt> zulaessigeLehraemter = Set.of(LehrerLehramt.ID_04, LehrerLehramt.ID_08, LehrerLehramt.ID_90, LehrerLehramt.ID_98);
	private static final @NotNull Set<LehrerLehrbefaehigung> zuPruefendeLehrbefaehigungen = Set.of(LehrerLehrbefaehigung.AE, LehrerLehrbefaehigung.MG, LehrerLehrbefaehigung.NG,
			LehrerLehrbefaehigung.SB);

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehrbefaehigung     eine Lehrbefaehigung des Lehrers
	 * @param lehrerLehramt       das Lehramt des Lehrers
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@NotNull LehrerLehrbefaehigung> lehrbefaehigung,
			final @NotNull Supplier<@AllowNull LehrerLehramt> lehrerLehramt,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_lehrbefaehigung = lehrbefaehigung;
		_lehrerLehramt = lehrerLehramt;
	}

	@Override
	protected boolean pruefe() {

		if (zulaessigeLehraemter.contains(_lehrerLehramt.get())) {

			if (!zuPruefendeLehrbefaehigungen.contains(_lehrbefaehigung.get())) {
				addFehler(0,
						"Bei den Lehrbefähigungen 'AE - Ästhetische Erziehung', 'MG - Mathematische Grundbildung', 'NG - Natur- und Gesellschaftswissenschaften' und 'SB - Sprachliche Grundbildung' muss das Lehramt 'Grundschule' oder 'Sonderpädagogische Förderung' bzw. die Lehramtseinträge 'Studierende' oder 'Lehramtsanwärter/-in / Studienreferendar/-in' angegeben werden.");
				return false;
			}
		}
		return true;
	}
}
