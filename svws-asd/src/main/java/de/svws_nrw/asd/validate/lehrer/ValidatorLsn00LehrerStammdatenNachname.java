package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsn00LehrerStammdatenNachname extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<@AllowNull String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn00LehrerStammdatenNachname(final @NotNull Supplier<@AllowNull String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLsn10LehrerStammdatenNachname(getNotNullSupplier(daten), kontext));
	}

	@Override
	protected boolean pruefe() {
		final String nachname = daten.get();

		if ((nachname == null) || (nachname.isEmpty())) {
			addFehler(0, "Nachname der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
