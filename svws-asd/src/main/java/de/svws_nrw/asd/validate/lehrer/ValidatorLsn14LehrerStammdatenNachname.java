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
public final class ValidatorLsn14LehrerStammdatenNachname extends Validator {

	/** Der Lehrer-Nachname */
	private final @NotNull Supplier<String> daten;
	private static final @NotNull  String FEHLERTEXT =
			"Nachname der Lehrkraft: Die zweite Stelle des Nachnamens ist mit einem Großbuchstaben besetzt. Bitte stellen sie sicher, dass nur der erste Buchstabe des Nachnamens ein Großbuchstabe ist. Bitte schreiben Sie auf ihn folgende Buchstaben klein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     der Nachname des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public ValidatorLsn14LehrerStammdatenNachname(final @NotNull Supplier<String> daten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	@Override
	protected boolean pruefe() {
		final @NotNull String nachnameOhneZusatz = NamensManager.getOhneZusatz(daten.get());

		if (nachnameOhneZusatz.length() > 1 && Character.isUpperCase(nachnameOhneZusatz.charAt(1))) {
			addFehler(5, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
