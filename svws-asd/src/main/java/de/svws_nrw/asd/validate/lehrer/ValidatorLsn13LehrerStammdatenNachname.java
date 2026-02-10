package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.NamensManager;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsn13LehrerStammdatenNachname extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<String> daten;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn13LehrerStammdatenNachname(final @NotNull Supplier<String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}


	@Override
	protected boolean pruefe() {
		final @NotNull String nachnameOhneZusatz = NamensManager.getOhneZusatz(daten.get());

		if (!Character.isUpperCase(nachnameOhneZusatz.charAt(0))) {
			addFehler(4,
					"Nachname der Lehrkraft: Die erste Stelle des Nachnamens muss - ggf. im Anschluss an einen Namenszusatz, wie z.B. \"von\" -  mit einem Großbuchstaben besetzt sein.");
			return false;
		}

		return true;
	}

}
