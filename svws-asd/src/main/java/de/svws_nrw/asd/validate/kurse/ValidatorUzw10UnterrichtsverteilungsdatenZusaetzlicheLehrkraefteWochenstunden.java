package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden extends Validator {

	private final @NotNull Supplier<@NotNull Double> wochenstundenLehrer;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden des Lehrer
	 * @param kontext                 der Kontext des Validators
	 */
	public ValidatorUzw10UnterrichtsverteilungsdatenZusaetzlicheLehrkraefteWochenstunden(final @NotNull Supplier<@NotNull Double> wochenstundenLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenLehrer = wochenstundenLehrer;
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final Double wochenstunden = wochenstundenLehrer.get();

		if (Double.compare(wochenstunden, 0.0) < 0) {
			addFehler(0, "Wochenstunden der zusätzlichen Lehrkraft: Der eingetragene Wert muss mindestens '0' betragen.");
			return false;
		}

		return true;
	}

}
