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
public final class ValidatorLpl01LehrerPersonaldatenLehramt extends Validator {

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
	public ValidatorLpl01LehrerPersonaldatenLehramt(final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter, final @NotNull Supplier<Long> lehrerId, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.lehraemter = lehraemter;
		this.lehrerId = lehrerId;
	}


	@Override
	protected boolean pruefe() {
		final @NotNull Schulform schulform = kontext().getSchulform();
		final boolean istFW = Schulform.FW.equals(schulform);
		final int anzahlLehraemter = this.lehraemter.get().size();

		// Fehlerkürzel: LPL1 Bei Freien Waldorfschulen darf kein Lehramt erfasst sein
		// FW: KEIN Lehramt erlaubt
		if (istFW && anzahlLehraemter > 0) {
			this.addFehler(1, "Bei Freien Waldorfschulen darf kein Lehramt erfasst sein. Lehrer ID: " + this.lehrerId.get());
			return false;
		}

		return true;
	}

}
