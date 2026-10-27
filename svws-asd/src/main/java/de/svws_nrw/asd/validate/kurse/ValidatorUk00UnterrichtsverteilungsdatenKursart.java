package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob das Fach zulässig, bzw. in der JSON-Datei vorhanden ist.
 */
public final class ValidatorUk00UnterrichtsverteilungsdatenKursart extends Validator {

	private final @NotNull Supplier<@AllowNull String> _kursartAllg;

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Kursart.
	 *
	 * @param schuljahr     Schuljahr
	 * @param kursartAllg   die allgemeine Kursart
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorUk00UnterrichtsverteilungsdatenKursart(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull String> kursartAllg,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_kursartAllg = kursartAllg;

		_validatoren.add(new ValidatorUk01UnterrichtsverteilungsdatenKursart(schuljahr, getNotNullSupplier(kursartAllg), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Bestimme kursartAllg.
		final @AllowNull String kursartAllg = _kursartAllg.get();

			if (kursartAllg == null || kursartAllg.isEmpty()) {
				addFehler(0, "Kursart der Unterrichtsverteilungsdaten: Kein Wert vorhanden.");
				return false;
			}
		return true;
	}
}
