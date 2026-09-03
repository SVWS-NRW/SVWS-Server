package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {

	private final @NotNull Supplier<@AllowNull Integer> wochenstundenLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden(final @NotNull Supplier<@AllowNull Integer> wochenstundenLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
		_validatoren.add(new ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(getNotNullSupplierInteger(wochenstundenLehrer), kontext));
	}

	@Override
	protected boolean pruefe() {

		final Integer wochenstunden = wochenstundenLehrer.get();

		if ((wochenstunden == null)) {
			addFehler(0, "Wochenstunden der Lehrkraft: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
