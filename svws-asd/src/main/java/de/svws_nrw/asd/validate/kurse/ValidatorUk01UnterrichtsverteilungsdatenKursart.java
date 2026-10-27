package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die Kursart zulässig, bzw. in der JSON-Datei vorhanden ist.
 */

public final class ValidatorUk01UnterrichtsverteilungsdatenKursart extends Validator {

	private final @NotNull Supplier<@NotNull String> _kursartAllg;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung des Fachs.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   die allgemeine Kursart
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUk01UnterrichtsverteilungsdatenKursart(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull String> kursartAllg,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_kursartAllg = kursartAllg;

		_validatoren.add(
				new ValidatorUk02UnterrichtsverteilungsdatenKursart(schuljahr, kursartAllg, kontext));

	}

	@Override
	protected boolean pruefe() {
		final @NotNull String kursartAllg = _kursartAllg.get();

		if (!ZulaessigeKursart.isKursartAllgVorhanden(kursartAllg)) {
			addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Das Feld 'Kursart' muss zulässig sein.");
			return false;
		}

		return true;
	}
}
