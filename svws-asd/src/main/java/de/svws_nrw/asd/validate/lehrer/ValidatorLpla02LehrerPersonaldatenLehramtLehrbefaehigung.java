package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.asd.types.lehrer.LehrerLehrbefaehigung;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Lehrbefähigung im zeitlichen Raum der JSON-Datei liegt.
 */
public final class ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/** Die Liste der Lehrämter. */
	private final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<@AllowNull List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;

		_validatoren.add(new ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		_validatoren.add(new ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		_validatoren.add(new ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		_validatoren.add(new ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	@Override
	protected boolean pruefe() {

		boolean fehlerVorhanden = false;

		List<LehrerLehramtEintrag> lehrerLehramtEintragList = lehraemter.get();

		if (lehrerLehramtEintragList != null) {

			for (final @NotNull LehrerLehramtEintrag lehrerLehramtEintrag : lehrerLehramtEintragList) {

				for (LehrerLehrbefaehigungEintrag lehrerLehrbefaehigungEintrag : lehrerLehramtEintrag.lehrbefaehigungen) {

					LehrerLehrbefaehigung lehrerLehrbefaehigung =
							LehrerLehrbefaehigung.data().getWertByIDOrNull(lehrerLehrbefaehigungEintrag.idLehrbefaehigung);

					if (lehrerLehrbefaehigung == null) {
						// die idLehrbefaehigung befindet sich gar nicht im Katalog LehrerLehrbefaehigungen.json
						fehlerVorhanden = true;
						addFehler(0, "Der eingetragene Wert für das Feld 'Lehrbefähigungen' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
					} else {
						if (lehrerLehrbefaehigung.daten(kontext().getSchuljahr()) == null) {
							// die zu überprüfende idLehrbehaehigung gilt nicht im zu überprüfenden Zeitraum
							fehlerVorhanden = true;
							addFehler(0, "Der eingetragene Wert für das Feld 'Lehrbefähigungen' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
						}
					}
				}

				if (fehlerVorhanden) {
					return false;
				}

			}
		}
		return true;
	}
}
