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
 * Prüft, ob die Lehrbefähigung im zeitlichen Raum der JSON-Datei liegt.
 */
public final class ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	@Override
	protected boolean pruefe() {

		List<LehrerLehramtEintrag> lehrerLehramtEintragList = lehraemter.get();

		if (lehrerLehramtEintragList != null) {
			for (final LehrerLehramtEintrag lehrerLehramtEintrag : lehrerLehramtEintragList) {

				LehrerLehramt lehrerLehramt = LehrerLehramt.data().getWertByID(lehrerLehramtEintrag.idKatalogLehramt);

				if (LehrerLehramt.ID_70.equals(lehrerLehramt)) {

					for (final LehrerLehrbefaehigungEintrag lehrerLehrbefaehigungEintrag : lehrerLehramtEintrag.lehrbefaehigungen) {
						if (!LehrerLehrbefaehigung.OA.equals(LehrerLehrbefaehigung.data().getWertByID(lehrerLehrbefaehigungEintrag.idLehrbefaehigung))) {
							addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' ist nur die Lehrbefähigung 'ohne Angabe' zulässig.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
