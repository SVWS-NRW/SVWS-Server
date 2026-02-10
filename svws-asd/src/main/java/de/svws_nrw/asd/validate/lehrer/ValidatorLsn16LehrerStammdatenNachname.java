package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsn16LehrerStammdatenNachname extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn16LehrerStammdatenNachname(final @NotNull Supplier<String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final String nachname = daten.get();

		if (nachname.contains(" -") || nachname.contains("- ")) {
			addFehler(7, "Nachname der Lehrkraft: Der Nachname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			return false;
		}

		return true;
	}

}
