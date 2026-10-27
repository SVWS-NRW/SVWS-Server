package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Kursart des Kurses aus.
 */
public final class ValidatorUkUnterrichtsverteilungsdatenKursart extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param kursartAllg              Die Kursart
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorUkUnterrichtsverteilungsdatenKursart(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull String> kursartAllg,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () ->  kontext.getSchuljahrBySchuljahresabschnitt(getNotNullSupplierLong(idSchuljahresabschnitt));


		_validatoren.add(new ValidatorUk00UnterrichtsverteilungsdatenKursart(schuljahr, kursartAllg, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
