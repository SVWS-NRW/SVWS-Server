package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Kürzel der Klasse aus
 */
public final class ValidatorKa11KlassenAbkuerzung extends Validator {

	/** das Kürzel der Klasse */
	private final @NotNull Supplier<@NotNull String> kuerzel;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorKa11KlassenAbkuerzung(final @NotNull Supplier<@NotNull String> kuerzel,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.kuerzel = kuerzel;
	}

	@Override
	protected boolean pruefe() {

		if (kuerzel.get().length() > 15) {

			addFehler(0, "Kürzel der Klasse: Das eingetragene Kürzel darf maximal 15 Stellen besitzen.");
			return false;
		}

		return true;
	}

}
