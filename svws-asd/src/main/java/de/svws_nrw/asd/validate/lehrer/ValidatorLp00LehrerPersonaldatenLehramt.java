package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLp00LehrerPersonaldatenLehramt extends Validator {

	/** Die Lehrämter */
	private final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter;

	/** Die LehrerId */
	private final @NotNull Supplier<Long> lehrerId;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param lehrerId   			die LehrerId
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLp00LehrerPersonaldatenLehramt(
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull Supplier<Long> lehrerId,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
		this.lehrerId = lehrerId;
	}

	@Override
	protected boolean pruefe() {
		// Fehlerkürzel: LP00 Zu jeder Lehrkraft muss mindestens ein Lehramt vorliegen.
		final @NotNull Schulform schulform = kontext().getSchulform();
		final boolean istFW = Schulform.FW.equals(schulform);
		final int anzahlLehraemter = this.lehraemter.get().size();

		// Alle Schulformen außer FW: MINDESTENS ein Lehramt erforderlich
		if (!istFW && anzahlLehraemter == 0) {
			this.addFehler(0, "Zu jeder Lehrkraft muss mindest ein Lehramt vorliegen. Lehrer ID: " + this.lehrerId.get());
			return false;
		}

		return true;
	}

}
