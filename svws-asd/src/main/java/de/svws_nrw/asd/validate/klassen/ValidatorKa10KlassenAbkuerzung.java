package de.svws_nrw.asd.validate.klassen;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Kürzel der Klasse aus
 */
public final class ValidatorKa10KlassenAbkuerzung extends Validator {

	/** das Kürzel der Klasse */
	private final @NotNull Supplier<@NotNull String> kuerzel;
	/** eine Liste der Kürzel der Klassen der Schule */
	private final @NotNull Supplier<@AllowNull List<String>> listKuerzel;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param listKuerzel eine Liste der Kürzel der Klassen der Schule
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorKa10KlassenAbkuerzung(final @NotNull Supplier<@NotNull String> kuerzel, final @NotNull Supplier<@AllowNull List<String>> listKuerzel,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.kuerzel = kuerzel;
		this.listKuerzel = listKuerzel;
	}

	@Override
	protected boolean pruefe() {

		int anzahl = 0;
		List<String> listeKuerzelString = listKuerzel.get();
		String zuPruefendesKuerzel = kuerzel.get();

		if (listeKuerzelString != null && !listeKuerzelString.isEmpty()) {

			for (String kuerzelAusListe : listeKuerzelString) {

				if (zuPruefendesKuerzel.equals(kuerzelAusListe)) {
					anzahl = anzahl + 1;
				}
			}

			if (anzahl > 1) {
				addFehler(0, "Kürzel der Klasse: Das eingetragene Kürzel kommt mehrfach vor.");
				return false;
			}
		}

		return true;
	}

}
