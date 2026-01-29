package de.svws_nrw.asd.validate.gesamt;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorGlpl00GesamtLehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.listLehrer = listLehrer;
	}

	@Override
	protected boolean pruefe() {
		boolean success = true;
		final @NotNull List<LehrerStatistikGesamt> list = listLehrer.get();

		final @NotNull Schulform schulform = kontext().getSchulform();
		final boolean istFW = Schulform.FW.equals(schulform);

		for (final LehrerStatistikGesamt ls : list) {
			final int anzahlLehraemter = ls.lehraemter.size();

			// Alle Schulformen außer FW: MINDESTENS ein Lehramt erforderlich
			if (!istFW && anzahlLehraemter == 0) {
				this.addFehler(0, "Zu Jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + ls.id);
				success = false;
			}
		}

		return success;
	}

}
