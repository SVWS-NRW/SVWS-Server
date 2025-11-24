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
public final class ValidatorLpl01LehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrer-Personalabschnittsdaten */
	private final @NotNull LehrerPersonaldaten lehrerPersonaldaten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLpl01LehrerPersonaldatenLehramt(final @NotNull LehrerPersonaldaten lehrerPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	@Override
	protected boolean pruefe() {
		final @NotNull Schulform schulform = kontext().getSchulform();
		final boolean istFW = Schulform.FW.equals(schulform);
		final int anzahlLehraemter = lehrerPersonaldaten.lehraemter.size();

		// Fehlerkürzel: LPL1 Bei Freien Waldorfschulen darf kein Lehramt erfasst sein
		// FW: KEIN Lehramt erlaubt
		if (istFW && anzahlLehraemter > 0) {
			this.addFehler(1, "Bei Freien Waldorfschulen darf kein Lehramt erfasst sein. Lehrer ID: " + lehrerPersonaldaten.id);
			return false;
		}

		return true;
	}

}
