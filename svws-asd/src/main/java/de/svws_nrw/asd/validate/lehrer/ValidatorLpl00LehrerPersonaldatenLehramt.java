package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpl00LehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLpl00LehrerPersonaldatenLehramt(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	@Override
	protected boolean pruefe() {
		// Fehlerkürzel: LPL00 Zu jeder Lehrkraft muss mindestens ein Lehramt vorliegen.
		final @NotNull Schulform schulform = kontext().getSchulform();
		final boolean istFW = Schulform.FW.equals(schulform);
		final int anzahlLehraemter = lehrerPersonaldaten.lehraemter.size();

		// Alle Schulformen außer FW: MINDESTENS ein Lehramt erforderlich
		if (!istFW && anzahlLehraemter == 0) {
			this.addFehler(0, "Zu jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + lehrerPersonaldaten.id);
			return false;
		}

		return true;
	}

}
