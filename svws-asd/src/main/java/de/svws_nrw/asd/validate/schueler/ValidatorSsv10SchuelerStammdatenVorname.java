package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Vornamen bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsv10SchuelerStammdatenVorname extends Validator {

	/** Der Schueler-Vorname */
	private final @NotNull Supplier<String> vorname;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param vorname   der Vorname des Schuelers
	 * @param kontext   der Kontext der Schule
	 */
	public ValidatorSsv10SchuelerStammdatenVorname(final @NotNull Supplier<String> vorname,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.vorname = vorname;
	}

	@Override
	protected boolean pruefe() {
		if (vorname.get().trim().isBlank()) {
			addFehler(1, "Rufname des Schülers: Der Rufname darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}

		return true;
	}

}
