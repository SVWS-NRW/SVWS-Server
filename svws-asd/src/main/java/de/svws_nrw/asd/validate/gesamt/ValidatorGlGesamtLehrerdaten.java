package de.svws_nrw.asd.validate.gesamt;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Lehrern einer Schule aus.
 */
public final class ValidatorGlGesamtLehrerdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste aller Lehrerdaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGlGesamtLehrerdaten(final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorGldGesamtLehrerdatenDuplikate(listLehrer, kontext));
	}


	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
