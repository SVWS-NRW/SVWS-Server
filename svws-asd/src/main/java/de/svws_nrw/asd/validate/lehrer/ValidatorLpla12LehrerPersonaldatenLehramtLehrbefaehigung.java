package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehramt;
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

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(
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

				if (!LehrerLehramt.ID_30.equals(zuueberpruefendesLehramt) && !LehrerLehramt.ID_32.equals(zuueberpruefendesLehramt) && !LehrerLehramt.ID_35.equals(zuueberpruefendesLehramt)) {

					if (lehrerLehramtEintrag.lehrbefaehigungen.isEmpty()) {
						addFehler(0,
								"Das Feld 'Lehrbefähigung' darf nur bei den Lehrämtern 'Berufsbildende Schulen - altes Lehramt -', 'Sekundarstufe II (mit beruflicher Fachrichtung)' und 'Berufskolleg' leer sein.");
						return false;
					}
				}
			}
		}
		return true;
	}
}
