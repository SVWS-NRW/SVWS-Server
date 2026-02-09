package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsv17LehrerStammdatenVorname extends Validator {

	/** Der Vorname des Lehrers */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Vorname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsv17LehrerStammdatenVorname(final @NotNull Supplier<String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;

	}


	@Override
	protected boolean pruefe() {
		final String nLower = daten.get().toLowerCase();

		if (nLower.startsWith("frau ") || nLower.startsWith("herr ")) {
			addFehler(8, "Vorname der Lehrkraft: Die Anrede (Frau oder Herr) gehört nicht in den Vornamen.");
			return false;
		}

		return true;
	}

}
