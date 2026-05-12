package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
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
 * Prüft, ob für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' die Lehrbefähigung 'Betreuung' voliegt.
 */
public final class ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(
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

				if (LehrerLehramt.ID_63.equals(zuueberpruefendesLehramt) || LehrerLehramt.ID_64.equals(zuueberpruefendesLehramt) || LehrerLehramt.ID_65.equals(zuueberpruefendesLehramt)) {

					for (final LehrerLehrbefaehigungEintrag lehrerLehrbefaehigungEintrag : lehrerLehramtEintrag.lehrbefaehigungen) {

						LehrerLehrbefaehigung zuueberprufendeLehrbefaehigunhg = LehrerLehrbefaehigung.data().getWertByID(lehrerLehrbefaehigungEintrag.idLehrbefaehigung);

						if (!LehrerLehrbefaehigung.BE.equals(zuueberprufendeLehrbefaehigunhg)) {
							addFehler(0,
									"Für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' ist nur die Lehrbefähigung 'Betreuung' zulässig.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
