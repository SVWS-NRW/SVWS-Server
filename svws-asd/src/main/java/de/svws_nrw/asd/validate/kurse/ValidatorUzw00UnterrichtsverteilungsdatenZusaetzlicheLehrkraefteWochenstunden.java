package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden extends Validator {

	private final @NotNull Supplier<@AllowNull Double> wochenstundenLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden des Lehrer
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorUzw00UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(final @NotNull Supplier<@AllowNull Double> wochenstundenLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
		_validatoren.add(new ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(getNotNullSupplierDouble(wochenstundenLehrer), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final Double wochenstunden = wochenstundenLehrer.get();

		if ((wochenstunden == null)) {
			addFehler(0, "Wochenstunden der zusätzlichen Lehrkraft: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
