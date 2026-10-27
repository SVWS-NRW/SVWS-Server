package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Kursart zulässig, bzw. in der JSON-Datei für das jeweilige Jahr vorhanden ist.
 */

public final class ValidatorUk02UnterrichtsverteilungsdatenKursart extends Validator {

	/** Das aktuelle Schuljahr */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Fach */
	private final @NotNull Supplier<@NotNull String> _kursartAllg;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   FachID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUk02UnterrichtsverteilungsdatenKursart(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull String> kursartAllg,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_kursartAllg = kursartAllg;
		_schuljahr = schuljahr;

	}

	@Override
	protected boolean pruefe() {

		if (ZulaessigeKursart.getByAllgemeinerKursart(_schuljahr.get(), _kursartAllg.get()).isEmpty()) {
			addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Der eingetragene Wert für das Feld 'Kursart' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}

}
