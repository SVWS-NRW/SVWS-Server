package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsv16LehrerStammdatenVorname extends Validator {

	/** Der Vorname des Lehrers */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsv16LehrerStammdatenVorname(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;

	}


	@Override
	protected boolean pruefe() {
		final String vorname = daten.get();

		if (vorname.contains(" -") || vorname.contains("- ")) {
			addFehler(7, "Vorname der Lehrkraft: Der Vorname enthält überflüssige Leerzeichen vor und/oder nach dem Bindestrich.");
			return false;
		}

		return true;
	}

}
