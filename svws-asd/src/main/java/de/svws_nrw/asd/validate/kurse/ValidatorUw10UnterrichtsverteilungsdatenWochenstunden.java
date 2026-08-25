package de.svws_nrw.asd.validate.kurse;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung aus.
 */
public final class ValidatorUw10UnterrichtsverteilungsdatenWochenstunden extends Validator {

	private final @NotNull Supplier<@NotNull Double> wochenstundenKurs;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs     die Wochenstunden des Kurses
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorUw10UnterrichtsverteilungsdatenWochenstunden(final @NotNull Supplier<@NotNull Double> wochenstundenKurs, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.wochenstundenKurs = wochenstundenKurs;
	}

	@Override
	protected boolean pruefe() {
		// Prüfe, ob die Schulform überhaupt gesetzt ist oder nicht
		final Double wochenstunden = wochenstundenKurs.get();

		if (Double.compare(wochenstunden, 0.0) < 0) {
			addFehler(0, "Wochenstunden des Kurses: Der eingetragene Wert muss mindestens '0' betragen.");
			return false;
		}

		return true;
	}

}
