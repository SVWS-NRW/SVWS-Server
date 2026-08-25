package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUw00UnterrichtsverteilungsdatenWochenstunden extends Validator {

	private final @NotNull Supplier<@AllowNull Double> wochenstundenKurs;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs     die Wochenstunden des Kurses
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorUw00UnterrichtsverteilungsdatenWochenstunden(final @NotNull Supplier<@AllowNull Double> wochenstundenKurs, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenKurs = wochenstundenKurs;
		_validatoren.add(new ValidatorUw10UnterrichtsverteilungsdatenWochenstunden(getNotNullSupplierDouble(wochenstundenKurs), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final Double wochenstunden = wochenstundenKurs.get();

		if ((wochenstunden == null)) {
			addFehler(0, "Wochenstunden des Kurses: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
