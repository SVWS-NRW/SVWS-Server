package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsv00LehrerStammdatenVorname extends Validator {

	/** Der Lehrer-Vorname */
	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsv00LehrerStammdatenVorname(final @NotNull Supplier<@AllowNull String> daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsv10LehrerStammdatenVorname(getNotNullSupplier(daten), kontext));

	}


	@Override
	protected boolean pruefe() {
		final String vorname = daten.get();

		if (vorname == null || vorname.length() == 0) {
			addFehler(0, "Vorname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
