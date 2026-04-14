package de.svws_nrw.asd.validate.gesamt;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Gesamtdaten
 * zu Schülern einer Schule aus.
 */
public final class ValidatorGsGesamtSchuelerdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listSchueler        die Liste aller Schülerdaten
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGsGesamtSchuelerdaten(final @NotNull Supplier<List<SchuelerStatistikGesamt>> listSchueler, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorGsdGesamtSchuelerdatenDuplikate(listSchueler, kontext));
	}


	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
