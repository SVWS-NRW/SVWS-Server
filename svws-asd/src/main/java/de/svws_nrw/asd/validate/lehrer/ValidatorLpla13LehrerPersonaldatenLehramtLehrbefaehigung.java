package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
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

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	private static final @NotNull Set<LehrerLehramt> zulaessigeLehraemter = Set.of(LehrerLehramt.ID_04, LehrerLehramt.ID_08, LehrerLehramt.ID_90, LehrerLehramt.ID_98);
	private static final @NotNull Set<LehrerLehrbefaehigung> zuPruefendeLehrbefaehigungen = Set.of(LehrerLehrbefaehigung.AE, LehrerLehrbefaehigung.MG, LehrerLehrbefaehigung.NG,
			LehrerLehrbefaehigung.SB);

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		final List<LehrerLehramtEintrag> liste = this.lehraemter.get();

		if (liste != null) {

			for (final LehrerLehramtEintrag lehrerLehramtEintrag : liste) {

				final LehrerLehramt zuueberpruefendesLehramt = LehrerLehramt.data().getWertByIDOrNull(lehrerLehramtEintrag.idKatalogLehramt);

				if (!zulaessigeLehraemter.contains(zuueberpruefendesLehramt)) {

					for (final LehrerLehrbefaehigungEintrag lehrerLehrbefaehigungEintrag : lehrerLehramtEintrag.lehrbefaehigungen) {

						if (zuPruefendeLehrbefaehigungen
								.contains((LehrerLehrbefaehigung.data().getWertByIDOrNull(lehrerLehrbefaehigungEintrag.idLehrbefaehigung)))) {
							addFehler(0,
									"Bei den Lehrbefähigungen 'AE - Ästhetische Erziehung', 'MG - Mathematische Grundbildung', 'NG - Natur- und Gesellschaftswissenschaften' und 'SB - Sprachliche Grundbildung' muss das Lehramt 'Grundschule' oder 'Sonderpädagogische Förderung' bzw. die Lehramtseinträge 'Studierende' oder 'Lehramtsanwärter/-in / Studienreferendar/-in' angegeben werden.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
