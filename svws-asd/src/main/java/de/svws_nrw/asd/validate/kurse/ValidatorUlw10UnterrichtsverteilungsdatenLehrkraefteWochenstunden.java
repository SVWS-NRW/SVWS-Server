package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {

	private final @NotNull Supplier<@NotNull Integer> wochenstundenLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorUlw10UnterrichtsverteilungsdatenLehrkraefteWochenstunden(final @NotNull Supplier<@NotNull Integer> wochenstundenLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
	}

	@Override
	protected boolean pruefe() {

		final Integer wochenstunden = wochenstundenLehrer.get();

		if (Integer.compare(wochenstunden, 0) < 0) {
			addFehler(0, "Wochenstunden der Lehrkraft: Es sind nur Werte >= 0 erlaubt");
			return false;
		}

		return true;
	}

}
